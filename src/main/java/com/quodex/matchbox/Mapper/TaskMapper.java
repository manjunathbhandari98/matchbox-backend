package com.quodex.matchbox.Mapper;

import com.quodex.matchbox.dto.request.TaskRequest;
import com.quodex.matchbox.dto.response.*;
import com.quodex.matchbox.enums.ProjectPriority;
import com.quodex.matchbox.enums.TaskStatus;
import com.quodex.matchbox.model.*;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class TaskMapper {

    public static Task toEntity(TaskRequest request) {
        if (request == null) return null;

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO)
                .priority(request.getPriority() != null ? request.getPriority() : ProjectPriority.LOW)
                .startDate(request.getStartDate())
                .dueDate(request.getDueDate())
                .progress(request.getProgress() != null ? request.getProgress() : 0)
                .tags(request.getTags() != null ? request.getTags() : Set.of())
                .archived(false)
                .build();

        // Map project
        if (request.getProjectId() != null) {
            task.setProject(Project.builder().id(request.getProjectId()).build());
        }

        // Map multiple assigned users
        if (request.getAssignedToId() != null && !request.getAssignedToId().isEmpty()) {
            List<User> assignedUsers = request.getAssignedToId().stream()
                    .map(id -> User.builder().id(id).build())
                    .collect(Collectors.toList());
            task.setAssignedTo(assignedUsers);
        }

        // Map createdBy
        if (request.getCreatedById() != null) {
            task.setCreatedBy(User.builder().id(request.getCreatedById()).build());
        }

        // Map team
        if (request.getTeamId() != null) {
            task.setTeam(Team.builder().id(request.getTeamId()).build());
        }

        // Map parent task
        if (request.getParentTaskId() != null) {
            task.setParentTask(Task.builder().id(request.getParentTaskId()).build());
        }

        return task;
    }


    public static TaskResponse toResponse(Task entity) {
        if (entity == null) return null;

        return TaskResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .startDate(entity.getStartDate())
                .dueDate(entity.getDueDate())
                .completedAt(entity.getCompletedAt())
                .progress(entity.getProgress())
                .projectId(entity.getProject() != null ? entity.getProject().getId() : null)
                .projectName(entity.getProject() != null ? entity.getProject().getName() : null)
                .assignedTo(
                        entity.getAssignedTo() != null
                                ? entity.getAssignedTo().stream()
                                .map(u -> new CollaboratorResponse(u.getId(), u.getFullName(),u.getUsername(),u.getEmail(), u.getAvatar(), u.getBio()))
                                .collect(Collectors.toList())
                                : List.of()
                )
                .createdById(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null)
                .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
                .parentTaskId(entity.getParentTask() != null ? entity.getParentTask().getId() : null)
                .tags(entity.getTags())
                .subtasks(entity.getSubtasks() != null
                        ? entity.getSubtasks().stream()
                        .map(sub -> new SubtaskResponse(
                                sub.getId(),
                                sub.getTitle(),
                                sub.getStatus(),
                                sub.getProgress()
                        ))
                        .collect(Collectors.toList())
                        : List.of())
                .attachments(entity.getAttachments() != null
                        ? entity.getAttachments().stream()
                        .map(a -> new TaskAttachmentResponse(a.getId(), a.getName(), a.getUrl()))
                        .collect(Collectors.toList())
                        : List.of())
                .comments(entity.getComments() != null
                        ? entity.getComments().stream()
                        .map(c -> new TaskCommentResponse(
                                c.getId(),
                                c.getContent(),
                                c.getAuthorId() != null ? c.getAuthorId() : null,
                                c.getCreatedAt()
                        ))
                        .collect(Collectors.toList())
                        : List.of())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .archived(entity.isArchived())
                .build();
    }

}
