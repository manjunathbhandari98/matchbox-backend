package com.quodex.matchbox.controller;

import com.quodex.matchbox.dto.request.TaskRequest;
import com.quodex.matchbox.dto.response.ProjectResponse;
import com.quodex.matchbox.dto.response.TaskProgressSummaryResponse;
import com.quodex.matchbox.dto.response.TaskResponse;
import com.quodex.matchbox.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @RequestBody TaskRequest request){
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @GetMapping("/my-task/{userId}")
    public ResponseEntity<List<TaskResponse>> getMyTasks(@PathVariable String userId){
        return ResponseEntity.ok(taskService.getMyTasks(userId));
    }

    @GetMapping("/all-task/{userId}")
    public ResponseEntity<List<TaskResponse>> getAllTasks(@PathVariable String userId){
        return ResponseEntity.ok(taskService.getAllTasksFromAssignedProjects(userId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getTaskByProject(@PathVariable String projectId){
        return ResponseEntity.ok(taskService.getProjectsByProject(projectId));
    }

    @GetMapping("total-task/completed/{userId}")
    public ResponseEntity<Integer> getTotalCompletedTaskForuser(@PathVariable String userId){
        return ResponseEntity.ok(taskService.getTotalCompletedTasksForUser(userId));
    }

    @GetMapping("/users/{userId}/tasks/in-progress/summary")
    public ResponseEntity<TaskProgressSummaryResponse> getInProgressSummary(@PathVariable String userId) {
        TaskProgressSummaryResponse response = taskService.getInProgressTaskSummaryForUser(userId);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable String taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task Deleted Successfully");
    }
}
