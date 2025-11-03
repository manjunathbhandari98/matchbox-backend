package com.quodex.matchbox.service;

import com.quodex.matchbox.Mapper.TaskMapper;
import com.quodex.matchbox.dto.request.TaskRequest;
import com.quodex.matchbox.dto.response.TaskResponse;
import com.quodex.matchbox.model.Project;
import com.quodex.matchbox.model.Task;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.repository.ProjectRepository;
import com.quodex.matchbox.repository.TaskRepository;
import com.quodex.matchbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    public TaskResponse createTask(TaskRequest request) {
        Task task = TaskMapper.toEntity(request);
        taskRepository.save(task);
        return TaskMapper.toResponse(task);
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
    public void deleteTask(String taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tasks not found"));
        taskRepository.delete(task);
    }
}
