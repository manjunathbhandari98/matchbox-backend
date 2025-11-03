package com.quodex.matchbox.repository;

import com.quodex.matchbox.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,String> {
    List<Task> findByAssignedTo_Id(String userId);

    List<Task> findByProject_Id(String projectId);

    List<Task> findByProject_IdIn(List<String> projectIds);
}
