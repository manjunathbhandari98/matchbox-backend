package com.quodex.matchbox.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String fullName;
    private String email;
    private String username;
    private String avatar;
    private String bio;
    private boolean active;
    private UserSettingsRequest settingsRequest;

}
