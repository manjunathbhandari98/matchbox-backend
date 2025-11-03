package com.quodex.matchbox.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorResponse {
    private String id;
    private String fullName;
    private String username;
    private String email;
    private String avatar;
    private String bio;
}
