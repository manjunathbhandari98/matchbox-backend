package com.quodex.matchbox.model;

import com.quodex.matchbox.enums.InvitationStatus;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "invited_user_id", nullable = false)
    private User invitedUser;

    @ManyToOne
    @JoinColumn(name = "inviter_id", nullable = false)
    private User inviter;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status = InvitationStatus.PENDING;
    private LocalDateTime invitedAt = LocalDateTime.now();
    private LocalDateTime acceptedAt;
}
