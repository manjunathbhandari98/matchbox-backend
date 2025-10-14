package com.quodex.matchbox.dto;

import com.quodex.matchbox.enums.UserRole;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private UserRole role;
}
