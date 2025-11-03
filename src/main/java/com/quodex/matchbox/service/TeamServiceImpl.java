package com.quodex.matchbox.service;

import com.quodex.matchbox.Mapper.TeamMapper;
import com.quodex.matchbox.dto.request.AddMemberRequest;
import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.TeamResponse;
import com.quodex.matchbox.enums.InvitationStatus;
import com.quodex.matchbox.enums.NotificationType;
import com.quodex.matchbox.enums.Role;
import com.quodex.matchbox.model.Invitation;
import com.quodex.matchbox.model.Team;
import com.quodex.matchbox.model.TeamMember;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.repository.InvitationRepository;
import com.quodex.matchbox.repository.TeamMemberRepository;
import com.quodex.matchbox.repository.TeamRepository;
import com.quodex.matchbox.repository.UserRepository;
import com.quodex.matchbox.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final InvitationRepository invitationRepository;
    private final NotificationService notificationService;
    private final SlugUtils slugUtils;

    @Override
    public TeamResponse createTeam(TeamRequest request) {
        if (request.getCreatedBy() == null || request.getCreatedBy().isBlank()) {
            throw new RuntimeException("CreatedBy field is required");
        }

        Team team = TeamMapper.toEntity(request);

        String slug = slugUtils.generateUniqueSlug(
                team.getName(),
                teamRepository::existsBySlug
        );

        team.setSlug(slug);
        teamRepository.save(team);

        List<TeamMember> teamMembers = request.getMembers().stream()
                .map(m -> {
                    User user = userRepository.findById(m.getId())
                            .orElseThrow(() -> new RuntimeException("User not found: " + m.getId()));

                    return TeamMember.builder()
                            .team(team)
                            .user(user)
                            .teamRole(m.getRole())
                            .status(InvitationStatus.ACCEPTED)
                            .invitedAt(LocalDateTime.now())
                            .acceptedAt(LocalDateTime.now())
                            .build();
                })
                .collect(Collectors.toList());

        teamMemberRepository.saveAll(teamMembers);
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
    public TeamResponse getTeamById(String teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team Not Found"));

        return TeamMapper.toResponse(team);
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
    public List<Invitation> getAcceptedMembersOfInviter(String senderId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return invitationRepository.findByInviter(sender);
    }

    @Override
    public String deleteTeam(String creatorId, String teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (!team.getCreatedBy().equals(creatorId)) {
            throw new RuntimeException("Only the team creator can delete this team");
        }

        teamRepository.delete(team);
        return "Team deleted successfully";
    }

    @Override
    public TeamResponse updateTeam(String teamId, String name, String description, MultipartFile avatar) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        team.setName(name);
        team.setDescription(description);

        // Handle avatar update
        if (avatar != null) {
            if (!avatar.isEmpty()) {
                // Save new avatar file
                try {
                    String uploadDir = "uploads/avatars/";
                    String fileName = UUID.randomUUID() + "_" + avatar.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir + fileName);

                    Files.createDirectories(filePath.getParent());
                    Files.write(filePath, avatar.getBytes());

                    team.setAvatar("/" + uploadDir + fileName);
                } catch (IOException e) {
                    throw new RuntimeException("Error saving avatar file: " + e.getMessage(), e);
                }
            } else {
                // Avatar is explicitly empty → remove existing avatar
                team.setAvatar(null);
            }
        }
        // else avatar param is null → do not change the existing avatar

        team.setSlug(slugUtils.generateUniqueSlug(name, teamRepository::existsBySlug));
        team.setUpdatedAt(LocalDateTime.now());

        teamRepository.save(team);
        return TeamMapper.toResponse(team);
    }

    @Override
    public String deleteTeamMember(String teamId, String memberId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Find the team member by userId
        Optional<TeamMember> memberToRemove = team.getMembers().stream()
                .filter(member -> member.getUser().getId().equals(memberId))
                .findFirst();

        if (memberToRemove.isEmpty()) {
            return "Member not found in team";
        }

        // Delete from the TeamMemberRepository (important!)
        teamMemberRepository.delete(memberToRemove.get());

        // Optionally, update the team object too
        team.getMembers().remove(memberToRemove.get());
        teamRepository.save(team);

        return "Member removed successfully";
    }

    public TeamResponse updateMemberRole(String teamId, String memberId, Role role){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Find the team member by userId
        TeamMember memberToUpdate = team.getMembers().stream()
                .filter(member -> member.getUser().getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Member not found"));
//
//        if (memberToUpdate.isEmpty()) {
//            throw new RuntimeException("");
//        }

        memberToUpdate.setTeamRole(role);

        teamMemberRepository.save(memberToUpdate);

        team.setUpdatedAt(LocalDateTime.now());
        teamRepository.save(team);

        return TeamMapper.toResponse(team);
    }

    @Override
    public TeamResponse addMemberToTeam(String teamId, AddMemberRequest request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        User user = userRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean alreadyMember = team.getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(request.getMemberId()));

        if (alreadyMember) {
            throw new RuntimeException("Member already exists");
        }

        TeamMember newMember = TeamMember.builder()
                .team(team)
                .user(user)
                .teamRole(request.getRole()) // convert string to enum
                .status(InvitationStatus.ACCEPTED)
                .invitedAt(LocalDateTime.now())
                .acceptedAt(LocalDateTime.now())
                .build();

        teamMemberRepository.save(newMember);

        // Add to team's member list
        team.getMembers().add(newMember);
        team.setUpdatedAt(LocalDateTime.now());

        // Save the team
        teamRepository.save(team);

        return TeamMapper.toResponse(team);
    }


}
