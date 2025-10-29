package com.quodex.matchbox.Mapper;
import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.ProjectSummaryResponse;
import com.quodex.matchbox.dto.response.TeamResponse;
import com.quodex.matchbox.model.Team;
import java.util.stream.Collectors;
import com.quodex.matchbox.model.TeamMember;
import java.util.Collections;

public class TeamMapper {

    // Convert DTO → Entity
    public static Team toEntity(TeamRequest request) {
        return Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .avatar(request.getAvatar())
                .createdBy(request.getCreatedBy())
                .projects(request.getProjects())
                .build();
    }

    // Convert Entity → DTO
    public static TeamResponse toResponse(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .avatar(team.getAvatar())
                .createdBy(team.getCreatedBy())
                .members(
                        team.getMembers() != null
                                ? team.getMembers().stream()
                                .map(TeamMember::getUser) // get User from TeamMember
                                .map(UserMapper::toUserResponse) // map User → UserResponse
                                .collect(Collectors.toSet())
                                : Collections.emptySet()
                )
                .projects(
                        team.getProjects() != null
                                ? team.getProjects().stream()
                                .map(p -> new ProjectSummaryResponse(p.getId(), p.getName(), p.getSlug()))
                                .collect(Collectors.toList())
                                : Collections.emptyList()
                )
                .createdAt(team.getCreatedAt())
                .updatedAt(team.getUpdatedAt())
                .build();
    }
}
