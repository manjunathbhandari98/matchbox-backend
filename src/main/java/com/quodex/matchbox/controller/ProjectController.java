package com.quodex.matchbox.controller;

import com.quodex.matchbox.dto.request.ProjectRequest;
import com.quodex.matchbox.dto.response.ProjectResponse;
import com.quodex.matchbox.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping()
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request){
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @GetMapping("/user")
    public ResponseEntity<List<ProjectResponse>> getProjectsForUser(
            @RequestParam("userId") String userId
    ) {
        List<ProjectResponse> projects = projectService.getProjectsForUser(userId);
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable String projectId,
                                                         @RequestBody ProjectRequest request){
        return ResponseEntity.ok(projectService.updateProject(projectId,request));
    }
}
