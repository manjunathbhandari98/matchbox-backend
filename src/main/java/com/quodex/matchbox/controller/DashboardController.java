package com.quodex.matchbox.controller;

import com.quodex.matchbox.dto.response.DeadlineResponse;
import com.quodex.matchbox.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/users/{userId}/upcoming-deadlines")
    public ResponseEntity<List<DeadlineResponse>> getUpcomingDeadlines(@PathVariable String userId) {
        List<DeadlineResponse> deadlines = dashboardService.getUpcomingDeadlines(userId);
        return ResponseEntity.ok(deadlines);
    }
}
