package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.request.TaskRequest;
import com.quodex.matchbox.dto.response.DeadlineResponse;
import com.quodex.matchbox.dto.response.TaskProgressSummaryResponse;
import com.quodex.matchbox.dto.response.TaskResponse;

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
}
