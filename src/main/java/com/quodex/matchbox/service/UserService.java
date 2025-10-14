package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.LoginRequest;
import com.quodex.matchbox.dto.LoginResponse;
import com.quodex.matchbox.dto.RegisterRequest;

public interface UserService {
    String registerUser(RegisterRequest request);

    LoginResponse loginUser(LoginRequest request);
}
