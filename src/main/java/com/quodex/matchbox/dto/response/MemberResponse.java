package com.quodex.matchbox.dto.response;

import com.quodex.matchbox.enums.InvitationStatus;
import com.quodex.matchbox.enums.Role;
import com.quodex.matchbox.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private String id;
    private String fullName;
    private String username;
    private String email;
    private String avatar;
    private String bio;
    private boolean active;
    private LocalDateTime lastSeen;
    private UserRole role;
    private Role teamRole;
    private InvitationStatus invitationStatus;
}