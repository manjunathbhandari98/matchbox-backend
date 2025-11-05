package com.quodex.matchbox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OverviewResponse {
     private Long totalTasks;
    private Long completedTasks;
    private Double completionRate;
    private Double avgTimePerTask;
    private Long activeMembers ;
    private Integer taskChangeSinceLastMonth;
    private Double timeImprovement;
}
