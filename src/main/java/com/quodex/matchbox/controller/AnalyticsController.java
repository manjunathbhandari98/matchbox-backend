package com.quodex.matchbox.controller;

import com.quodex.matchbox.dto.response.*;
import com.quodex.matchbox.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/overview/{userId}")
    public ResponseEntity<OverviewResponse> getOverview(@PathVariable String userId){
        return ResponseEntity.ok(analyticsService.getOverview(userId));
    }

    @GetMapping("/team-performance/{userId}")
    public ResponseEntity<List<TeamPerformanceResponse>> getTeamPerformance(@PathVariable String userId) {
        return ResponseEntity.ok(analyticsService.getTeamPerformance(userId));
    }

    @GetMapping("/project-progress/{userId}")
    public ResponseEntity<List<ProjectProgressResponse>> getProjectProgress(@PathVariable String userId) {
        return ResponseEntity.ok(analyticsService.getProjectProgress(userId));
    }

    @GetMapping("/weekly-summary/{userId}")
    public ResponseEntity<WeeklySummaryResponse> getWeeklySummary(@PathVariable String userId) {
        return ResponseEntity.ok(analyticsService.getWeeklySummary(userId));
    }

    @GetMapping("/top-performer/{userId}")
    public ResponseEntity<TopPerformerResponse> getTopPerformer(@PathVariable String userId) {
        return ResponseEntity.ok(analyticsService.getTopPerformer(userId));
    }

    @GetMapping("/overall-health/{userId}")
    public ResponseEntity<OverallHealthResponse> getOverallHealth(@PathVariable String userId) {
        return ResponseEntity.ok(analyticsService.getOverallHealth(userId));
    }
}

