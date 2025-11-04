package com.quodex.matchbox.service;

import com.quodex.matchbox.Mapper.TaskMapper;
import com.quodex.matchbox.dto.request.TaskRequest;
import com.quodex.matchbox.dto.response.DeadlineResponse;
import com.quodex.matchbox.dto.response.TaskProgressSummaryResponse;
import com.quodex.matchbox.dto.response.TaskResponse;
import com.quodex.matchbox.enums.TaskStatus;
import com.quodex.matchbox.model.Project;
import com.quodex.matchbox.model.Task;
import com.quodex.matchbox.model.Team;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.repository.ProjectRepository;
import com.quodex.matchbox.repository.TaskRepository;
import com.quodex.matchbox.repository.TeamRepository;
import com.quodex.matchbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        // Convert request to entity
        Task task = TaskMapper.toEntity(request);

        // Fetch project
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + request.getProjectId()));

        // --- Update project task counters ---
        project.setTotalTasks(project.getTotalTasks() + 1);

        if (request.getStatus() == TaskStatus.COMPLETED) {
            project.setCompletedTasks(project.getCompletedTasks() + 1);
        }


        if (request.getAssignedToId() != null && !request.getAssignedToId().isEmpty()) {
            project.setAssignedTasks(project.getAssignedTasks() + 1);
        }

        // Save both task and project
        task.setProject(project);
        Task savedTask = taskRepository.save(task);
        projectRepository.save(project);

        // Return response
        return TaskMapper.toResponse(savedTask);
    }


    @Override
    public List<TaskResponse> getMyTasks(String userId) {
        List<Task> tasks = taskRepository.findByAssignedTo_Id(userId);
        return tasks.stream()
                .map(TaskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getAllTasksFromAssignedProjects(String userId) {
        // Step 1: Find all projects where this user is a collaborator or creator
        List<Project> userProjects = projectRepository.findByCreatorIdOrCollaborators_Id(userId, userId);

        if (userProjects.isEmpty()) {
            return Collections.emptyList();
        }

        // Step 2: Extract project IDs
        List<String> projectIds = userProjects.stream()
                .map(Project::getId)
                .toList();

        // Step 3: Find all tasks belonging to these projects
        List<Task> tasks = taskRepository.findByProject_IdIn(projectIds);

        // Step 4: Convert to response DTOs
        return tasks.stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getProjectsByProject(String projectId){
        List<Task> tasks = taskRepository.findByProject_Id(projectId);
        return tasks.stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalCompletedTasksForUser(String userId) {
        // Step 1: Validate user
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        //  Step 2: Find all projects where the user is involved (creator or collaborator)
        List<Project> userProjects = projectRepository.findByCreatorIdOrCollaborators_Id(userId, userId);

        // If no projects, return 0
        if (userProjects.isEmpty()) {
            return 0;
        }

        // Step 3: Extract project IDs
        List<String> projectIds = userProjects.stream()
                .map(Project::getId)
                .toList();

        // Step 4: Count completed tasks from those projects
        long completedCount = taskRepository.findByProject_IdIn(projectIds)
                .stream()
                .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                .count();

        return (int) completedCount;
    }

    @Override
    @Transactional(readOnly = true)
    public TaskProgressSummaryResponse getInProgressTaskSummaryForUser(String userId) {
        //  Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        //  Get all team IDs where user is a member
        List<String> userTeamIds = teamRepository.findTeamsByMemberId(userId)
                .stream()
                .map(Team::getId)
                .toList();

        //  Get all projects the user is part of (creator, collaborator, or team)
        List<Project> userProjects = projectRepository.findAll().stream()
                .filter(project ->
                        project.getCreatorId().equals(userId)
                                || (project.getCollaborators() != null &&
                                project.getCollaborators().stream().anyMatch(c -> c.getId().equals(userId)))
                                || (project.getTeam() != null && userTeamIds.contains(project.getTeam().getId()))
                )
                .toList();

        if (userProjects.isEmpty()) {
            return new TaskProgressSummaryResponse(0, 0); // No projects or tasks
        }

        //  Extract project IDs
        List<String> projectIds = userProjects.stream().map(Project::getId).toList();

        //  Get all tasks under these projects that are IN_PROGRESS
        List<Task> inProgressTasks = taskRepository.findByProject_IdIn(projectIds)
                .stream()
                .filter(task -> task.getStatus() == TaskStatus.IN_PROGRESS)
                .toList();

        //  Count distinct project IDs among in-progress tasks
        long distinctProjects = inProgressTasks.stream()
                .map(task -> task.getProject().getId())
                .distinct()
                .count();

        //  Build response
        return new TaskProgressSummaryResponse(inProgressTasks.size(), (int) distinctProjects);
    }




    @Override
    public void deleteTask(String taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tasks not found"));
        taskRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeadlineResponse> getUpcomingTaskDeadlinesForUser(String userId) {
        // Validate user
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Find all projects user is part of (creator or collaborator)
        List<Project> userProjects = projectRepository.findByCreatorIdOrCollaborators_Id(userId, userId);
        if (userProjects.isEmpty()) return List.of();

        List<String> projectIds = userProjects.stream()
                .map(Project::getId)
                .toList();

        // Find all tasks with upcoming due dates (and not completed)
        List<Task> tasks = taskRepository.findByProject_IdIn(projectIds).stream()
                .filter(task -> task.getDueDate() != null && task.getStatus() != TaskStatus.COMPLETED)
                .sorted(Comparator.comparing(Task::getDueDate))
                .toList();

        // Map to DTO
        return tasks.stream()
                .map(task -> new DeadlineResponse(
                        task.getId(),
                        task.getTitle(),
                        "TASK",
                        task.getDueDate(),
                        task.getProject() != null ? task.getProject().getName() : null,
                        task.getStatus() == TaskStatus.COMPLETED
                ))
                .toList();
    }

}
