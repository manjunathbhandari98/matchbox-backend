package com.quodex.matchbox.controller;

import com.quodex.matchbox.dto.request.LoginRequest;
import com.quodex.matchbox.dto.response.LoginResponse;
import com.quodex.matchbox.dto.request.RegisterRequest;
import com.quodex.matchbox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request){
        return ResponseEntity.ok(userService.loginUser(request));
    }
}
