package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.response.*;
import com.quodex.matchbox.enums.TaskStatus;
import com.quodex.matchbox.model.Project;
import com.quodex.matchbox.model.Task;
import com.quodex.matchbox.model.TeamMember;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.repository.ProjectRepository;
import com.quodex.matchbox.repository.TaskRepository;
import com.quodex.matchbox.repository.TeamRepository;
import com.quodex.matchbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService{
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TeamService teamService;

    @Override
    public OverviewResponse getOverview(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        List<Task> userTask = taskRepository.findTasksForUser(userId);
        long totalTasks = userTask.size();
        long completedTasks = userTask.stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                .count();
        Double completionRate = totalTasks == 0 ? 0 : (completedTasks * 100.00 / totalTasks);

        Long activeMembers = Long.valueOf(teamService.getTotalActiveMembersAcrossTeams(userId));

        double avgTimePerTask = 3.2;

        return OverviewResponse.builder()
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .completionRate(completionRate)
                .avgTimePerTask(avgTimePerTask)
                .activeMembers(activeMembers)
                .taskChangeSinceLastMonth(10)
                .timeImprovement(0.5)
                .build();
    }

    @Override
    public List<TeamPerformanceResponse> getTeamPerformance(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        // Get all team members linked to this user
        Set<TeamMember> teamMembers = getAllMembersFromUserTeams(user);

        // Extract unique users (avoid duplicates across teams)
        Set<User> uniqueUsers = teamMembers.stream()
                .map(TeamMember::getUser)
                .collect(Collectors.toSet());

        List<TeamPerformanceResponse> responses = new ArrayList<>();

        for (User memberUser : uniqueUsers) {
            long assigned = taskRepository.countByAssignedToContains(memberUser);
            long completed = taskRepository.countByAssignedToContainsAndStatus(memberUser, TaskStatus.COMPLETED);
            double efficiency = assigned == 0 ? 0 : (completed * 100.0 / assigned);

            responses.add(
                    TeamPerformanceResponse.builder()
                            .memberId(memberUser.getId())
                            .fullName(memberUser.getFullName())
                            .avatar(memberUser.getAvatar())
                            .tasksAssigned(assigned)
                            .tasksCompleted(completed)
                            .efficiency(efficiency)
                            .build()
            );
        }

        return responses;
    }



    @Override
    public List<ProjectProgressResponse> getProjectProgress(String userId) {
        List<Project> userProjects = projectRepository.findByCreatorIdOrCollaborators_Id(userId, userId);

        return userProjects.stream().map(project -> {
            double completion = project.getTotalTasks() == 0
                    ? 0
                    : (project.getCompletedTasks() * 100.0 / project.getTotalTasks());

            double onTimeRate = 80; // compute later from task deadlines

            return ProjectProgressResponse.builder()
                    .projectId(project.getId())
                    .projectName(project.getName())
                    .completion(completion)
                    .onTimeRate(onTimeRate)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public WeeklySummaryResponse getWeeklySummary(String userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = now.minusDays(7);

        long tasksCompleted = taskRepository.countByUserAndStatusAndUpdatedAtBetween(userId, TaskStatus.COMPLETED, weekStart, now);
        long newTasks = taskRepository.countByUserAndCreatedAtBetween(userId, weekStart, now);
        long overdueTasks = taskRepository.countByUserAndDueDateBeforeAndStatusNot(userId, now, TaskStatus.COMPLETED);

        return WeeklySummaryResponse.builder()
                .tasksCompleted(tasksCompleted)
                .newTasks(newTasks)
                .overdueTasks(overdueTasks)
                .build();
    }

    @Override
    public TopPerformerResponse getTopPerformer(String userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<TeamMember> members = getAllMembersFromUserTeams(currentUser);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minusDays(7);

        return members.stream()
                .map(member -> {
                    User memberUser = member.getUser();

                    long assigned = taskRepository.countByAssignedToContains(memberUser);
                    long completed = taskRepository.countByAssignedToContainsAndStatus(memberUser, TaskStatus.COMPLETED);
                    double efficiency = assigned == 0 ? 0 : (completed * 100.0 / assigned);

                    // Actual tasks completed in the last 7 days
                    long completedThisWeek = taskRepository.countCompletedTasksInDateRange(
                            memberUser, TaskStatus.COMPLETED, weekAgo, now);

                    return new AbstractMap.SimpleEntry<>(memberUser, new Object[] {
                            efficiency, completedThisWeek
                    });
                })
                .max(Comparator.comparingDouble(e -> (double) e.getValue()[0])) // highest efficiency
                .map(entry -> {
                    User memberUser = entry.getKey();
                    double efficiency = (double) entry.getValue()[0];
                    long tasksCompletedThisWeek = (long) entry.getValue()[1];

                    return TopPerformerResponse.builder()
                            .memberId(memberUser.getId())
                            .fullName(memberUser.getFullName())
                            .avatar(memberUser.getAvatar())
                            .efficiency(efficiency)
                            .tasksCompletedThisWeek(tasksCompletedThisWeek)
                            .build();
                })
                .orElse(null);
    }


    @Override
    public OverallHealthResponse getOverallHealth(String userId) {
        List<Project> projects = projectRepository.findByCreatorIdOrCollaborators_Id(userId, userId);
        double avgCompletion = projects.stream()
                .mapToDouble(p -> p.getTotalTasks() == 0 ? 0 : (p.getCompletedTasks() * 100.0 / p.getTotalTasks()))
                .average().orElse(0);

        String grade = avgCompletion > 90 ? "A+" : avgCompletion > 75 ? "A" : "B";
        String comment = switch (grade) {
            case "A+" -> "Excellent team performance";
            case "A" -> "Strong progress with minor delays";
            default -> "Needs improvement on task completion";
        };

        return OverallHealthResponse.builder()
                .grade(grade)
                .onTimeDelivery(avgCompletion)
                .teamSatisfaction(4.5)
                .comment(comment)
                .build();
    }

    private Set<TeamMember> getAllMembersFromUserTeams(User user) {
        return teamRepository.findTeamsByUser(user)
                .stream()
                .flatMap(team -> team.getMembers().stream())
                .collect(Collectors.toSet());
    }


}
