package com.quodex.matchbox.dto.response;

import com.quodex.matchbox.enums.InvitationStatus;
import com.quodex.matchbox.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SearchUserResponse {
    private String fullName;
    private String username;
    private String email;
    private String avatar;
    private String bio;
    private boolean active;
    private LocalDateTime lastSeen;
    private UserRole role;
    private InvitationStatus invitationStatus;
}
