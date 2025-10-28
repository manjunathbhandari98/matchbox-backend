package com.quodex.matchbox.Mapper;

import com.quodex.matchbox.dto.request.ProjectRequest;
import com.quodex.matchbox.dto.response.ProjectResponse;
import com.quodex.matchbox.model.Project;
import com.quodex.matchbox.model.User;

import java.util.List;

public class ProjectMapper {


    public static Project toEntity(ProjectRequest dto) {
        return Project.builder()
                .name(dto.getName())
                .slug(dto.getSlug())
                .description(dto.getDescription())
                .creatorId(dto.getCreatorId())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .visibility(dto.getVisibility())
                .startDate(dto.getStartDate())
                .dueDate(dto.getDueDate())
                .build();
    }

    public static ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .slug(project.getSlug())
                .description(project.getDescription())
                .creatorId(project.getCreatorId())
                .teamId(project.getTeam() != null ? project.getTeam().getId() : null)
                .collaborators(project.getCollaborators())
                .status(project.getStatus())
                .priority(project.getPriority())
                .visibility(project.getVisibility())
                .startDate(project.getStartDate())
                .dueDate(project.getDueDate())
                .completedDate(project.getCompletedDate())
                .progress(project.getProgress())
                .totalTasks(project.getTotalTasks())
                .assignedTasks(project.getAssignedTasks())
                .completedTasks(project.getCompletedTasks())
                .overdueTasks(project.getOverdueTasks())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
