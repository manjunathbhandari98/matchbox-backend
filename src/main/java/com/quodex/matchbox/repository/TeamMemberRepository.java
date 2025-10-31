package com.quodex.matchbox.repository;

import com.quodex.matchbox.model.Team;
import com.quodex.matchbox.model.TeamMember;
import com.quodex.matchbox.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {
    boolean existsByTeamAndUser(Team team, User user);


}
