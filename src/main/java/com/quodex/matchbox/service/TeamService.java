package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.TeamResponse;

import java.util.List;

public interface TeamService {
    TeamResponse createTeam(TeamRequest request);

    List<TeamResponse> getTeams();
    List<TeamResponse> getTeamForUser(String userId);
}
