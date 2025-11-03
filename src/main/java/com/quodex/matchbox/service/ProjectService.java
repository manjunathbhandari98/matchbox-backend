package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.request.ProjectRequest;
import com.quodex.matchbox.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest request);

    List<ProjectResponse> getProjectsForUser(String userId);

    ProjectResponse getProjectById(String projectId);

    ProjectResponse updateProject(String projectId, ProjectRequest request);

    void deleteProject(String projectId);

    List<ProjectResponse> getProjectsByTeam(String teamId);
}
