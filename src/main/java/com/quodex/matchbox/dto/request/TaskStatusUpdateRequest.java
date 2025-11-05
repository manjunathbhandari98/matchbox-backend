package com.quodex.matchbox.dto.request;

import com.quodex.matchbox.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskStatusUpdateRequest {
    private TaskStatus status;
}

