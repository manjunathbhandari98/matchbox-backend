package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.request.LoginRequest;
import com.quodex.matchbox.dto.request.RegisterRequest;
import com.quodex.matchbox.dto.request.UpdatePasswordRequest;
import com.quodex.matchbox.dto.request.UserRequest;
import com.quodex.matchbox.dto.response.LoginResponse;
import com.quodex.matchbox.dto.response.UserResponse;

public interface UserService {
    String registerUser(RegisterRequest request);

    LoginResponse loginUser(LoginRequest request);

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(String email, UserRequest request);

    String updatePassword(String email, UpdatePasswordRequest request);

}
