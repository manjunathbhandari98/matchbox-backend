package com.quodex.matchbox.dto.request;

import com.quodex.matchbox.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequest {
    private String id;
    private Role role;
}
