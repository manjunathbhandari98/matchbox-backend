package com.quodex.matchbox.model;

import com.quodex.matchbox.enums.ProjectPriority;
import com.quodex.matchbox.enums.ProjectStatus;
import com.quodex.matchbox.enums.Visibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String slug;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private String creatorId;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "project_collaborators",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private List<User> collaborators = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.IN_PROGRESS;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectPriority priority = ProjectPriority.LOW;

    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private LocalDateTime completedDate;

    @Builder.Default
    private Integer progress = 0;

    @Builder.Default
    private Integer totalTasks = 0;

    @Builder.Default
    private Integer assignedTasks = 0;

    @Builder.Default
    private Integer completedTasks = 0;

    @Builder.Default
    private Integer overdueTasks = 0;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProjectAttachment> attachments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Visibility visibility = Visibility.PRIVATE;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProjectPermissions> permissions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = ProjectStatus.IN_PROGRESS;
        }
        if (this.priority == null) {
            this.priority = ProjectPriority.LOW;
        }
        if (this.visibility == null) {
            this.visibility = Visibility.PRIVATE;
        }
        if (this.progress == null) {
            this.progress = 0;
        }
        if (this.totalTasks == null) {
            this.totalTasks = 0;
        }
        if (this.assignedTasks == null) {
            this.assignedTasks = 0;
        }
        if (this.completedTasks == null) {
            this.completedTasks = 0;
        }
        if (this.overdueTasks == null) {
            this.overdueTasks = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Helper method to add collaborators
    public void addCollaborator(User user) {
        if (this.collaborators == null) {
            this.collaborators = new ArrayList<>();
        }
        if (!this.collaborators.contains(user)) {
            this.collaborators.add(user);
        }
    }

    // Helper method to remove collaborators
    public void removeCollaborator(User user) {
        if (this.collaborators != null) {
            this.collaborators.remove(user);
        }
    }
}