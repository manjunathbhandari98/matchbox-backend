package com.quodex.matchbox.service;

import com.quodex.matchbox.Mapper.TeamMapper;
import com.quodex.matchbox.dto.request.TeamRequest;
import com.quodex.matchbox.dto.response.TeamResponse;
import com.quodex.matchbox.model.Team;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.repository.TeamRepository;
import com.quodex.matchbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    public TeamResponse createTeam(TeamRequest request) {
        Team team = TeamMapper.toEntity(request);
        Set<String> validMemberId = request.getMembers().stream()
                .filter(userRepository::existsById)
                .collect(Collectors.toSet());
        if (validMemberId.isEmpty()) {
            throw new RuntimeException("Member Not found for this team");
        }
        team.setMembers(validMemberId);
        teamRepository.save(team);
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
}
