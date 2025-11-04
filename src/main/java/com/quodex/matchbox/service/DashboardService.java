package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.response.DeadlineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TaskService taskService;
    private final ProjectService projectService;

    @Transactional(readOnly = true)
    public List<DeadlineResponse> getUpcomingDeadlines(String userId) {
        List<DeadlineResponse> taskDeadlines = taskService.getUpcomingTaskDeadlinesForUser(userId);
        List<DeadlineResponse> projectDeadlines = projectService.getUpcomingProjectDeadlinesForUser(userId);

        // Merge and sort all by dueDate ascending
        return Stream.concat(taskDeadlines.stream(), projectDeadlines.stream())
                .sorted(Comparator.comparing(DeadlineResponse::getDueDate))
                .limit(10) // Optional: only show top 10 soonest deadlines
                .toList();
    }
}