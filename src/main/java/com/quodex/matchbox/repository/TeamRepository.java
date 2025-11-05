package com.quodex.matchbox.repository;

import com.quodex.matchbox.model.Team;
import com.quodex.matchbox.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TeamRepository extends JpaRepository<Team, String> {
//    @Query("SELECT t FROM Team t JOIN t.members m WHERE m.id = :userId")
//    List<Team> findTeamsByMemberId(@Param("userId") String userId);

//    List<Team> findByMembersContaining(String userId);

    @Query("SELECT t FROM Team t JOIN t.members m WHERE m.user.id = :userId")
    List<Team> findTeamsByMemberId(@Param("userId") String userId);


    boolean existsBySlug(String slug);

    @Query("SELECT t FROM Team t JOIN t.members m WHERE m.user = :user")
    List<Team> findTeamsByUser(@Param("user") User user);


}
