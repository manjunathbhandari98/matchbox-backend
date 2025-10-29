package com.quodex.matchbox.service;

import com.quodex.matchbox.Mapper.TeamMapper;
import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.TeamResponse;
import com.quodex.matchbox.enums.InvitationStatus;
import com.quodex.matchbox.enums.NotificationType;
import com.quodex.matchbox.model.Invitation;
import com.quodex.matchbox.model.Team;
import com.quodex.matchbox.model.TeamMember;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.repository.InvitationRepository;
import com.quodex.matchbox.repository.TeamMemberRepository;
import com.quodex.matchbox.repository.TeamRepository;
import com.quodex.matchbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final InvitationRepository invitationRepository;
    private final NotificationService notificationService;

    @Override
    public TeamResponse createTeam(TeamRequest request) {
        // Convert DTO -> Entity
        Team team = TeamMapper.toEntity(request);

        // Validate creator
        if (request.getCreatedBy() == null || request.getCreatedBy().isBlank()) {
            throw new RuntimeException("CreatedBy field is required");
        }

        // Validate and fetch members
        List<User> validMembers = request.getMembers().stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found: " + userId)))
                .toList();

        if (validMembers.isEmpty()) {
            throw new RuntimeException("No valid members found for this team");
        }

        // Save the team first
        teamRepository.save(team);

        // Create TeamMember entries
        List<TeamMember> teamMembers = validMembers.stream()
                .map(user -> TeamMember.builder()
                        .team(team)
                        .user(user)
                        .status(InvitationStatus.ACCEPTED) // Since added directly while creating team
                        .build())
                .collect(Collectors.toList());

        teamMemberRepository.saveAll(teamMembers);

        // Attach them to team for response mapping
        team.setMembers(teamMembers);

        return TeamMapper.toResponse(team);
    }

    @Override
    public List<TeamResponse> getTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams.stream().map(TeamMapper::toResponse).toList();
    }

    @Override
    public List<TeamResponse> getTeamForUser(String userId) {
        // Validate user existence
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Fetch only relevant teams (instead of filtering all)
        List<Team> teams = teamRepository.findAll().stream()
                .filter(team -> {

                    if (team.getMembers() != null && team.getMembers().contains(userId)) {
                        return true;
                    }
                            return team.getCreatedBy().equals(userId);
                        }
                )
                .toList();

        //  Map to responses
        return teams.stream()
                .map(TeamMapper::toResponse)
                .toList();
    }

    @Override
    public String inviteMemberToTeam(String teamId, String userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (teamMemberRepository.existsByTeamAndUser(team, user)) {
            return "User already invited or part of the team";
        }

        TeamMember member = TeamMember.builder()
                .team(team)
                .user(user)
                .status(InvitationStatus.PENDING)
                .build();

        teamMemberRepository.save(member);
        return "Team invitation sent successfully";
    }

    @Override
    public String inviteUserToWorkspace(String inviterId, String invitedEmail) {
        User inviter = userRepository.findById(inviterId)
                .orElseThrow(() -> new RuntimeException("Inviter not found"));

        Optional<User> invitedUser = userRepository.findAll()
                .stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(invitedEmail))
                .findFirst();

        if (invitedUser.isPresent()) {
            if (invitationRepository.existsByInvitedUserAndStatus(invitedUser.get(), InvitationStatus.PENDING)) {
                return "User already invited";
            }
            Invitation invitation = Invitation.builder()
                    .invitedUser(invitedUser.get())
                    .inviter(inviter)
                    .status(InvitationStatus.PENDING)
                    .build();
            invitationRepository.save(invitation);

            notificationService.sendNotification(
                    inviter.getId(),
                    invitedUser.get().getId(),
                    NotificationType.INVITE,
                    "Workspace Invitation",
                    inviter.getFullName() + " has invited you to join their workspace.",
                    invitation.getId()
            );
            return "Invitation sent to existing user";
        } else {
            // Here you can send email invite to register
            return "User not found. Email invitation sent to join platform.";
        }
    }

    @Override
    public String acceptInvitation(String invitationId, String receiverId) {
        // Fetch invitation
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        // Check receiver
        if (!invitation.getInvitedUser().getId().equals(receiverId)) {
            throw new RuntimeException("You are not authorized to accept this invitation");
        }

        // Update invitation status
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setAcceptedAt(java.time.LocalDateTime.now());
        invitationRepository.save(invitation);

        // Team team = teamRepository.findByCreatedBy(invitation.getInviter().getId())
        //         .orElseThrow(() -> new RuntimeException("Inviter's team not found"));
        // TeamMember member = TeamMember.builder()
        //         .team(team)
        //         .user(invitation.getInvitedUser())
        //         .status(InvitationStatus.ACCEPTED)
        //         .build();
        // teamMemberRepository.save(member);

        // Notify inverter about acceptance
        notificationService.sendNotification(
                invitation.getInvitedUser().getId(),
                invitation.getInviter().getId(),
                NotificationType.INFO,
                "Invitation Accepted",
                invitation.getInvitedUser().getFullName() + " has accepted your workspace invitation.",
                invitation.getId()
        );

        return "Invitation accepted successfully!";
    }

    @Override
    public List<User> getAcceptedMembersOfInviter(String senderId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Invitation> acceptedInvites =
                invitationRepository.findByInviterAndStatus(sender, InvitationStatus.ACCEPTED);

        return acceptedInvites.stream()
                .map(Invitation::getInvitedUser)
                .collect(Collectors.toList());
    }

}
