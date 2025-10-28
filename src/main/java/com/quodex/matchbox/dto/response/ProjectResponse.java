package com.quodex.matchbox.dto.response;

import com.quodex.matchbox.enums.ProjectPriority;
import com.quodex.matchbox.enums.ProjectStatus;
import com.quodex.matchbox.enums.Visibility;
import com.quodex.matchbox.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private String id;
    private String name;
    private String slug;
    private String description;
    private String creatorId;
    private String teamId;

    private List<User> collaborators;

    private ProjectStatus status;
    private ProjectPriority priority;
    private Visibility visibility;

    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private LocalDateTime completedDate;

    private Integer progress;
    private Integer totalTasks;
    private Integer assignedTasks;
    private Integer completedTasks;
    private Integer overdueTasks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
