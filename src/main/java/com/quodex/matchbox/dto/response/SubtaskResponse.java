package com.quodex.matchbox.dto.response;

import com.quodex.matchbox.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubtaskResponse {
    private String id;
    private String title;
    private TaskStatus status;
    private Integer progress;
}

