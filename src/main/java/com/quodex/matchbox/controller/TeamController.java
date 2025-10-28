package com.quodex.matchbox.controller;

import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.TeamResponse;
import com.quodex.matchbox.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping()
    public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamRequest request){
        return ResponseEntity.ok(teamService.createTeam(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeamResponse>> getTeams(){
        return ResponseEntity.ok(teamService.getTeams());
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getTeamsForUser(@RequestParam("userId") String userId){
        List<TeamResponse> teams = teamService.getTeamForUser(userId);
        return ResponseEntity.ok(teams);
    }
}
