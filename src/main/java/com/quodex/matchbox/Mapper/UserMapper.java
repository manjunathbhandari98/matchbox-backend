package com.quodex.matchbox.Mapper;

import com.quodex.matchbox.dto.response.LoginResponse;
import com.quodex.matchbox.dto.request.RegisterRequest;
import com.quodex.matchbox.dto.response.UserResponse;
import com.quodex.matchbox.enums.UserRole;
import com.quodex.matchbox.model.User;


public class UserMapper {

    public static User toRegisterEntity(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .username(request.getUsername())
                .active(request.isActive())
                .role(UserRole.USER)
                .build();
    }

    public static LoginResponse toLoginResponse(User user, String token) {
        return LoginResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .token(token)
                .build();
    }

    public static UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .lastSeen(user.getLastSeen())
                .role(user.getRole())
                .email(user.getEmail())
                .active(user.isActive())
                .bio(user.getBio())
                .settings(user.getSettings() != null
                        ? UserSettingsMapper.toResponse(user.getSettings())
                        : null)
                .build();
    }

}
