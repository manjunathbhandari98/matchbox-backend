package com.quodex.matchbox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamPerformanceResponse {
    private String memberId;
    private String fullName;
    private String avatar;
    private long tasksCompleted;
    private long tasksAssigned;
    private double efficiency;
}