package com.quodex.matchbox.Mapper;

import com.quodex.matchbox.dto.LoginResponse;
import com.quodex.matchbox.dto.RegisterRequest;
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
}
