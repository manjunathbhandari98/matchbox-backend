package com.quodex.matchbox.controller;

import com.quodex.matchbox.Mapper.UserMapper;
import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.MemberResponse;
import com.quodex.matchbox.dto.response.TeamResponse;
import com.quodex.matchbox.dto.response.UserResponse;
import com.quodex.matchbox.model.Invitation;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/{teamId}/invite")
    public ResponseEntity<String> inviteMemberToTeam(
            @PathVariable String teamId,
            @RequestParam String userId) {
        return ResponseEntity.ok(teamService.inviteMemberToTeam(teamId, userId));
    }

    @PostMapping("/invite")
    public ResponseEntity<String> inviteUserToPlatform(
            @RequestParam String inviterId,
            @RequestParam String email) {
        return ResponseEntity.ok(teamService.inviteUserToWorkspace(inviterId, email));
    }

    @PostMapping("/invitations/{invitationId}/accept")
    public ResponseEntity<String> acceptInvitation(
            @PathVariable String invitationId,
            @RequestParam String receiverId
    ) {
        return ResponseEntity.ok(teamService.acceptInvitation(invitationId, receiverId));
    }

    @GetMapping("/members/{senderId}")
    public ResponseEntity<List<MemberResponse>> getMembersOfSender(@PathVariable String senderId) {
        List<Invitation> invitations = teamService.getAcceptedMembersOfInviter(senderId);

        List<MemberResponse> response = invitations.stream()
                .map(UserMapper::toMemberResponseFromInvitation)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


}
