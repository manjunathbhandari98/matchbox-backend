package com.quodex.matchbox.repository;

import com.quodex.matchbox.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {
}
