package com.quodex.matchbox.Mapper;

import com.quodex.matchbox.dto.request.ProjectRequest;
import com.quodex.matchbox.dto.response.CollaboratorResponse;
import com.quodex.matchbox.dto.response.ProjectResponse;
import com.quodex.matchbox.model.Project;
import com.quodex.matchbox.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                .collaborators(project.getCollaborators() != null ?
                        project.getCollaborators().stream()
                                .map(user -> CollaboratorResponse.builder()
                                        .id(user.getId())
                                        .fullName(user.getFullName())
                                        .username(user.getUsername())
                                        .email(user.getEmail())
                                        .avatar(user.getAvatar())
                                        .bio(user.getBio())
                                        .build())
                                .collect(Collectors.toList()) : new ArrayList<>())
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