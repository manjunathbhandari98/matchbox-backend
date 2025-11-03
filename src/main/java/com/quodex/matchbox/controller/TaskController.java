package com.quodex.matchbox.controller;

import com.quodex.matchbox.dto.request.TaskRequest;
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

    @GetMapping("/my-task/{userID}")
    public ResponseEntity<List<TaskResponse>> getMyTasks(@PathVariable String userId){
        return ResponseEntity.ok(taskService.getMyTasks(userId));
    }

    @GetMapping("/all-task/{userId}")
    public ResponseEntity<List<TaskResponse>> getAllTasks(@PathVariable String userId){
        return ResponseEntity.ok(taskService.getAllTasksFromAssignedProjects(userId));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable String taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task Deleted Successfully");
    }
}
