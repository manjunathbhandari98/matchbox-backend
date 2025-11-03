package com.quodex.matchbox.repository;

import com.quodex.matchbox.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findByCreatorIdOrCollaborators_Id(String userId, String userId1);

    List<Project> findByTeam_Id(String teamId);

    boolean existsBySlug(String existingSlug);
}
