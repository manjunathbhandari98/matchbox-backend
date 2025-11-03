package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.request.TaskRequest;
import com.quodex.matchbox.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);

    List<TaskResponse> getMyTasks(String userId);

    List<TaskResponse> getAllTasksFromAssignedProjects(String userId);

    void deleteTask(String taskId);
}
