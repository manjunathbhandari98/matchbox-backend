package com.quodex.matchbox.dto.request;

import com.quodex.matchbox.enums.ProjectPriority;
import com.quodex.matchbox.enums.ProjectStatus;
import com.quodex.matchbox.enums.Visibility;
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
public class ProjectRequest {
    private String name;
    private String slug;
    private String description;
    private String creatorId;
    private String teamId;
    private List<String> collaboratorIds;
    private ProjectStatus status = ProjectStatus.IN_PROGRESS;
    private ProjectPriority priority = ProjectPriority.LOW;
    private Visibility visibility = Visibility.PRIVATE;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
}
