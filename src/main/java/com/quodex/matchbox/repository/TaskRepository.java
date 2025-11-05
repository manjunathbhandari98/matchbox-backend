package com.quodex.matchbox.repository;

import com.quodex.matchbox.enums.TaskStatus;
import com.quodex.matchbox.model.Task;
import com.quodex.matchbox.model.TeamMember;
import com.quodex.matchbox.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task,String> {
    List<Task> findByAssignedTo_Id(String userId);

    List<Task> findByProject_Id(String projectId);

    List<Task> findByProject_IdIn(List<String> projectIds);

    long countByAssignedToContains(User user);

    long countByAssignedToContainsAndStatus(User member, TaskStatus taskStatus);

    @Query("SELECT t FROM Task t WHERE t.createdBy.id = :userId OR :userId IN (SELECT u.id FROM t.assignedTo u)")
    List<Task> findTasksForUser(@Param("userId") String userId);

    @Query("SELECT COUNT(t) FROM Task t WHERE (t.createdBy.id = :userId OR :userId IN (SELECT u.id FROM t.assignedTo u)) AND t.status = :status AND t.updatedAt BETWEEN :start AND :end")
    long countByUserAndStatusAndUpdatedAtBetween(@Param("userId") String userId, @Param("status") TaskStatus status, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(t) FROM Task t WHERE (t.createdBy.id = :userId OR :userId IN (SELECT u.id FROM t.assignedTo u)) AND t.createdAt BETWEEN :start AND :end")
    long countByUserAndCreatedAtBetween(@Param("userId") String userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(t) FROM Task t WHERE (t.createdBy.id = :userId OR :userId IN (SELECT u.id FROM t.assignedTo u)) AND t.dueDate < :now AND t.status <> :status")
    long countByUserAndDueDateBeforeAndStatusNot(@Param("userId") String userId, @Param("now") LocalDateTime now, @Param("status") TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t WHERE :user MEMBER OF t.assignedTo AND t.status = :status AND t.updatedAt BETWEEN :start AND :end")
    long countCompletedTasksInDateRange(
            @Param("user") User user,
            @Param("status") TaskStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    boolean existsBySlug(String slug);

    Task findBySlug(String slug);
}
