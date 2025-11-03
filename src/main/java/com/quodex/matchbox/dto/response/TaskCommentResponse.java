package com.quodex.matchbox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCommentResponse {
    private String id;
    private String commentText;
    private String createdById;
    private LocalDateTime createdAt;
}
