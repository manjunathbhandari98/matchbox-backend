package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.request.AddMemberRequest;
import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.TeamResponse;
import com.quodex.matchbox.enums.Role;
import com.quodex.matchbox.model.Invitation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeamService {
    TeamResponse createTeam(TeamRequest request);

    List<TeamResponse> getTeams();
    List<TeamResponse> getTeamForUser(String userId);

    String inviteMemberToTeam(String teamId, String userId);
    String inviteUserToWorkspace(String inviterId, String invitedEmail);
    String acceptInvitation(String invitationId, String receiverId);
    List<Invitation> getAcceptedMembersOfInviter(String senderId);

    String deleteTeam(String creatorId, String teamId);

    TeamResponse getTeamById(String teamId);

    TeamResponse updateTeam(String teamId, String name, String description, MultipartFile avatar);

    String deleteTeamMember(String teamId, String memberId);

    TeamResponse updateMemberRole(String teamId, String memberId, Role role);

    TeamResponse addMemberToTeam(String teamId, AddMemberRequest request);
}
