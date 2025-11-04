package com.quodex.matchbox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeadlineResponse {
    private String id;
    private String title;
    private String type; // "TASK" or "PROJECT"
    private LocalDateTime dueDate;
    private String name;
    private boolean completed;
}
