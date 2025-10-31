package com.quodex.matchbox.controller;

import com.quodex.matchbox.Mapper.UserMapper;
import com.quodex.matchbox.dto.request.AddMemberRequest;
import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.MemberResponse;
import com.quodex.matchbox.dto.response.TeamResponse;
import com.quodex.matchbox.enums.Role;
import com.quodex.matchbox.model.Invitation;
import com.quodex.matchbox.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable String teamId){
        return ResponseEntity.ok(teamService.getTeamById(teamId));
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

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTeam(@RequestParam String creatorId,
                                             @RequestParam String teamId){
        return ResponseEntity.ok(teamService.deleteTeam(creatorId,teamId));
    }

    @PutMapping(value = "/update/{teamId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TeamResponse> updateTeam(
            @PathVariable String teamId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        TeamResponse updated = teamService.updateTeam(teamId, name, description, avatar);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete-member")
    public ResponseEntity<String> deleteTeamMember(@RequestParam String teamId,
                                                   @RequestParam String memberId){
        return ResponseEntity.ok(teamService.deleteTeamMember(teamId, memberId));
    }

    @PutMapping("/update-role")
    public ResponseEntity<TeamResponse> updateMemberRole(@RequestParam String teamId,
                                                         @RequestParam String memberId,
                                                         @RequestBody Role role){
        return ResponseEntity.ok(teamService.updateMemberRole(teamId,memberId,role));
    }

    @PostMapping("/add-member")
    public ResponseEntity<TeamResponse> addMemberToTeam(@RequestParam String teamId,
                                                        @RequestBody AddMemberRequest request
                                                        ){
        return ResponseEntity.ok(teamService.addMemberToTeam(teamId, request));
    }

}
