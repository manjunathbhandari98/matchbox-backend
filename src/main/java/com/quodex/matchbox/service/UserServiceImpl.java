package com.quodex.matchbox.service;

import com.quodex.matchbox.Mapper.UserMapper;
import com.quodex.matchbox.Mapper.UserSettingsMapper;
import com.quodex.matchbox.dto.request.*;
import com.quodex.matchbox.dto.response.LoginResponse;
import com.quodex.matchbox.dto.response.MemberResponse;
import com.quodex.matchbox.dto.response.UserResponse;
import com.quodex.matchbox.enums.InvitationStatus;
import com.quodex.matchbox.jwt.JwtUtil;
import com.quodex.matchbox.model.Invitation;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.model.UserSettings;
import com.quodex.matchbox.repository.InvitationRepository;
import com.quodex.matchbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final InvitationRepository invitationRepository;

    @Override
    public String registerUser(RegisterRequest request) {
        // Check username validity and availability
        checkUsernameAvailability(request.getUsername());

        // Encode (hash) the password before saving
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);

        User user = UserMapper.toRegisterEntity(request);
        UserSettings settings = UserSettingsMapper.toDefaultSettings(user);
        user.setSettings(settings);
        settings.setUser(user);
        userRepository.save(user);

        return "Registered Successfully";
    }


    @Override
    public LoginResponse loginUser(LoginRequest request) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            );
            authManager.authenticate(authToken);
        } catch (AuthenticationException ex) {
            System.err.println("Authentication failed for email: " + request.getEmail());
            ex.printStackTrace();
            throw new RuntimeException("Invalid username or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        String token = jwtUtil.generateToken(request.getEmail(), user.getRole().name());

        return UserMapper.toLoginResponse(user, token);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        return UserMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(String email, UserRequest request) {
        // Fetch the user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        // Update user fields
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getBio() != null) user.setBio(request.getBio());
        user.setActive(request.isActive());
        if (request.getEmail() != null) user.setEmail(request.getEmail());

        // Update settings if present
        UserSettings settings = user.getSettings();
        UserSettingsRequest s = request.getSettingsRequest();
        if (s != null) {
            if (s.getTheme() != null) settings.setTheme(s.getTheme());
            settings.setEmailNotifications(s.isEmailNotifications());
            settings.setTaskAssignmentNotifications(s.isTaskAssignmentNotifications());
            settings.setProjectUpdateNotifications(s.isProjectUpdateNotifications());
            settings.setCommentsAndMentionNotifications(s.isCommentsAndMentionNotifications());
            settings.setWeeklySummary(s.isWeeklySummary());
            settings.setTwoFactorAuth(s.isTwoFactorAuth());
        }

        // Save user (settings saved automatically via CascadeType.ALL)
        userRepository.save(user);

        // Return updated response
        return UserMapper.toUserResponse(user);
    }

    @Override
    public String updatePassword(String email, UpdatePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User Not Found"));
//        Verfiy old password
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new RuntimeException("Incorrect Old Password");
        }

//        Check if old and new password are not same
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())){
            throw new RuntimeException("New Password can not be same as old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
         userRepository.save(user);
         return "Password Updated";
    }

    @Override
    public List<MemberResponse> searchUser(String query, String currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        List<User> users = userRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);

        return users.stream()
                .filter(user -> !user.getId().equals(currentUserId)) // exclude self
                .map(user -> {
                    InvitationStatus status = invitationRepository.findByInviterAndInvitedUser(currentUser, user)
                            .map(Invitation::getStatus)
                            .orElse(InvitationStatus.NONE); // Custom NONE enum

                    return MemberResponse.builder()
                            .fullName(user.getFullName())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .avatar(user.getAvatar())
                            .bio(user.getBio())
                            .active(user.isActive())
                            .lastSeen(user.getLastSeen())
                            .role(user.getRole())
                            .invitationStatus(status)
                            .build();
                })
                .toList();
    }


    private void checkUsernameAvailability(String username) {
        final String USERNAME_REGEX = "^(?=.{3,20}$)(?!.*[_.]{2})[a-z][a-z0-9._]*[a-z0-9]$";

        // Check pattern validity
        if (!username.matches(USERNAME_REGEX)) {
            throw new IllegalArgumentException("Invalid username format. " +
                    "Username must be 3–20 chars, lowercase, and can include numbers, '.' or '_'.");
        }

        // Check DB uniqueness
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already taken. Please choose another.");
        }
    }

}
