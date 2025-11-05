package com.quodex.matchbox.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quodex.matchbox.enums.InvitationStatus;
import com.quodex.matchbox.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "team_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "team"})
@EqualsAndHashCode(exclude = {"user", "team"})
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    @JsonIgnore // prevents infinite recursion
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_role")
    private Role teamRole;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status = InvitationStatus.PENDING;

    private LocalDateTime invitedAt = LocalDateTime.now();
    private LocalDateTime acceptedAt;
}
