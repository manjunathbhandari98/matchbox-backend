package com.quodex.matchbox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskProgressSummaryResponse {
    private Integer totalInProgressTasks;
    private Integer totalProjects;
}
