package com.quodex.matchbox.Mapper;
import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.MemberResponse;
import com.quodex.matchbox.dto.response.ProjectSummaryResponse;
import com.quodex.matchbox.dto.response.TeamResponse;
import com.quodex.matchbox.dto.response.UserResponse;
import com.quodex.matchbox.model.Team;

import java.util.List;
import java.util.stream.Collectors;
import com.quodex.matchbox.model.TeamMember;
import java.util.Collections;
public class TeamMapper {

    public static Team toEntity(TeamRequest request) {
        return Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .avatar(request.getAvatar())
                .createdBy(request.getCreatedBy())
                // Don't map members here - they'll be handled separately
                .build();
    }

    public static TeamResponse toResponse(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .avatar(team.getAvatar())
                .createdBy(team.getCreatedBy())
                .members(
                        team.getMembers().stream()
                                .map(tm -> MemberResponse.builder()
                                        .id(tm.getUser().getId())
                                        .fullName(tm.getUser().getFullName())
                                        .username(tm.getUser().getUsername())
                                        .email(tm.getUser().getEmail())
                                        .avatar(tm.getUser().getAvatar())
                                        .bio(tm.getUser().getBio())
                                        .active(tm.getUser().isActive())
                                        .lastSeen(tm.getUser().getLastSeen())
                                        .role(tm.getUser().getRole()) // system role
                                        .teamRole(tm.getTeamRole())
                                        .invitationStatus(tm.getStatus())
                                        .build()
                                )
                                .toList()
                )
                .createdAt(team.getCreatedAt())
                .updatedAt(team.getUpdatedAt())
                .build();
    }

}