package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.TeamResponse;
import com.quodex.matchbox.model.Invitation;
import com.quodex.matchbox.model.User;

import java.util.List;

public interface TeamService {
    TeamResponse createTeam(TeamRequest request);

    List<TeamResponse> getTeams();
    List<TeamResponse> getTeamForUser(String userId);

    String inviteMemberToTeam(String teamId, String userId);
    String inviteUserToWorkspace(String inviterId, String invitedEmail);
    String acceptInvitation(String invitationId, String receiverId);
    List<Invitation> getAcceptedMembersOfInviter(String senderId);
}
