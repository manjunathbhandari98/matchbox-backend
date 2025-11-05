package com.quodex.matchbox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklySummaryResponse {
    private long tasksCompleted;
    private long newTasks;
    private long overdueTasks;
}
