package com.quodex.matchbox.Mapper;

import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.ProjectSummaryResponse;
import com.quodex.matchbox.dto.response.TeamResponse;
import com.quodex.matchbox.model.Team;

public class TeamMapper {
    public static Team toEntity(TeamRequest request){
        return Team.builder()
                .name(request.getName())
                .avatar(request.getAvatar())
                .description(request.getDescription())
                .createdBy(request.getCreatedBy())
                .roles(request.getRoles())
                .members(request.getMembers())
                .projects(request.getProjects())
                .build();
    }

    public static TeamResponse toResponse(Team team){
        return TeamResponse.builder()
                .id(team.getId())
                .avatar(team.getAvatar())
                .updatedAt(team.getUpdatedAt())
                .createdAt(team.getCreatedAt())
                .createdBy(team.getCreatedBy())
                .members(team.getMembers())
                .roles(team.getRoles())
                .projects(
                        team.getProjects().stream()
                                .map(p -> new ProjectSummaryResponse(p.getId(), p.getName(), p.getSlug()))
                                .toList()
                )
                .description(team.getDescription())
                .name(team.getName())
                .build();
    }
}
