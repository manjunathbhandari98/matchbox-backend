package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.request.TaskRequest;
import com.quodex.matchbox.dto.response.DeadlineResponse;
import com.quodex.matchbox.dto.response.TaskProgressSummaryResponse;
import com.quodex.matchbox.dto.response.TaskResponse;
import com.quodex.matchbox.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);

    List<TaskResponse> getMyTasks(String userId);

    List<TaskResponse> getAllTasksFromAssignedProjects(String userId);

    void deleteTask(String taskId);

    List<TaskResponse> getProjectsByProject(String projectId);

    public Integer getTotalCompletedTasksForUser(String userId);

    TaskProgressSummaryResponse getInProgressTaskSummaryForUser(String userId);

    List<DeadlineResponse> getUpcomingTaskDeadlinesForUser(String userId);

    TaskResponse getTaskBySlug(String slug);

    TaskResponse updateTaskStatus(String taskId, TaskStatus status);
}
