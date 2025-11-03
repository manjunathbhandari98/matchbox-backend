package com.quodex.matchbox.dto.response;

import com.quodex.matchbox.enums.ProjectPriority;
import com.quodex.matchbox.enums.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private ProjectPriority priority;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
    private Integer progress;
    private String projectId;
    private List<String> assignedToId;
    private String createdById;
    private String teamId;
    private String parentTaskId;
    private Set<String> tags;
    private List<SubtaskResponse> subtasks;
    private List<TaskAttachmentResponse> attachments;
    private List<TaskCommentResponse> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean archived;
}
