package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.response.*;

import java.util.List;

public interface AnalyticsService {
    OverviewResponse getOverview(String userId);

    List<TeamPerformanceResponse> getTeamPerformance(String userId);

    List<ProjectProgressResponse> getProjectProgress(String userId);

    WeeklySummaryResponse getWeeklySummary(String userId);

    TopPerformerResponse getTopPerformer(String userId);

    OverallHealthResponse getOverallHealth(String userId);

}
