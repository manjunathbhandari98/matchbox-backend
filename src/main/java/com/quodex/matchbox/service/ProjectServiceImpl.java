package com.quodex.matchbox.service;

import com.quodex.matchbox.Mapper.ProjectMapper;
import com.quodex.matchbox.dto.request.ProjectRequest;
import com.quodex.matchbox.dto.response.ProjectResponse;
import com.quodex.matchbox.enums.Visibility;
import com.quodex.matchbox.model.Project;
import com.quodex.matchbox.model.Team;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.repository.ProjectRepository;
import com.quodex.matchbox.repository.TeamRepository;
import com.quodex.matchbox.repository.UserRepository;
import com.quodex.matchbox.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        // Validate creator exists
        User creator = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new RuntimeException("Creator not found with id: " + request.getCreatorId()));

        // Build base entity
        Project project = ProjectMapper.toEntity(request);

        // Ensure collaborators list is initialized
        if (project.getCollaborators() == null) {
            project.setCollaborators(new ArrayList<>());
        }

        // Generate slug
        String slug = SlugUtils.generateUniqueSlug(request.getName(), projectRepository::existsBySlug);
        project.setSlug(slug);

        // Set team if provided
        if (request.getTeamId() != null && !request.getTeamId().isEmpty()) {
            Team team = teamRepository.findById(request.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + request.getTeamId()));
            project.setTeam(team);
        }

        // Fetch collaborators
        if (request.getCollaboratorIds() != null && !request.getCollaboratorIds().isEmpty()) {
            List<User> collaborators = userRepository.findAllById(request.getCollaboratorIds());
            if (!collaborators.isEmpty()) {
                project.getCollaborators().addAll(collaborators);
            }
        }

        // Save project
        Project savedProject = projectRepository.save(project);
        log.info("Project created successfully with id: {}", savedProject.getId());

        return ProjectMapper.toResponse(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsForUser(String userId) {

        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Get all teams the user is part of
        List<String> userTeamIds = teamRepository.findTeamsByMemberId(userId)
                .stream()
                .map(Team::getId)
                .toList();

        log.info("User is part of {} teams", userTeamIds.size());

        // Get all visible projects
        List<Project> projects = projectRepository.findAll().stream()
                .filter(project -> {
                    // Public projects
                    if (project.getVisibility() == Visibility.PUBLIC) {
                        return true;
                    }

                    // Projects created by the user
                    if (project.getCreatorId().equals(userId)) {
                        return true;
                    }

                    // Projects where user is a collaborator
                    if (project.getCollaborators() != null &&
                            project.getCollaborators().stream()
                                    .anyMatch(c -> c.getId().equals(userId))) {
                        return true;
                    }

                    // Projects in teams the user is part of
                    if (project.getTeam() != null &&
                            userTeamIds.contains(project.getTeam().getId())) {
                        return true;
                    }

                    return false;
                })
                .toList();

        log.info("Found {} visible projects for user", projects.size());

        // Convert to DTOs
        return projects.stream()
                .map(ProjectMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(String projectId) {
        log.info("Fetching project with id: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        return ProjectMapper.toResponse(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByTeam(String teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team Not Found"));
        List<Project> projects = projectRepository.findByTeam_Id(teamId);
        return projects.stream()
                .map(ProjectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(String projectId, ProjectRequest request) {
        log.info("Updating project with id: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        // Update basic fields
        if (request.getName() != null) {
            project.setName(request.getName());
        }
        if (request.getSlug() != null) {
            project.setSlug(request.getSlug());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            project.setPriority(request.getPriority());
        }
        if (request.getVisibility() != null) {
            project.setVisibility(request.getVisibility());
        }
        if (request.getStartDate() != null) {
            project.setStartDate(request.getStartDate());
        }
        if (request.getDueDate() != null) {
            project.setDueDate(request.getDueDate());
        }

        // Update team if provided
        if (request.getTeamId() != null) {
            if (request.getTeamId().isEmpty()) {
                project.setTeam(null);
            } else {
                Team team = teamRepository.findById(request.getTeamId())
                        .orElseThrow(() -> new RuntimeException("Team not found with id: " + request.getTeamId()));
                project.setTeam(team);
            }
        }

        // Update collaborators if provided
        if (request.getCollaboratorIds() != null) {
            if (request.getCollaboratorIds().isEmpty()) {
                project.getCollaborators().clear();
            } else {
                List<User> collaborators = userRepository.findAllById(request.getCollaboratorIds());
                project.getCollaborators().clear();
                project.getCollaborators().addAll(collaborators);
            }
            log.info("Updated collaborators for project");
        }

        Project updatedProject = projectRepository.save(project);
        log.info("Project updated successfully");

        return ProjectMapper.toResponse(updatedProject);
    }

    @Override
    @Transactional
    public void deleteProject(String projectId) {
        log.info("Deleting project with id: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        projectRepository.delete(project);
        log.info("Project deleted successfully");
    }
}