package com.quodex.matchbox.service;

import com.quodex.matchbox.Mapper.UserMapper;
import com.quodex.matchbox.dto.LoginRequest;
import com.quodex.matchbox.dto.LoginResponse;
import com.quodex.matchbox.dto.RegisterRequest;
import com.quodex.matchbox.jwt.JwtUtil;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder; // <-- Inject PasswordEncoder

    @Override
    public String registerUser(RegisterRequest request) {
        // Encode (hash) the password before saving
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);

        User user = UserMapper.toRegisterEntity(request);
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
            throw new RuntimeException("Invalid username or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        String token = jwtUtil.generateToken(request.getEmail(), request.getRole().name());

        return UserMapper.toLoginResponse(user, token);
    }
}
