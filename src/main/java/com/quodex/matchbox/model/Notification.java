package com.quodex.matchbox.model;

import com.quodex.matchbox.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String title;
    private String message;
    @ManyToOne
    @JoinColumn(name = "invitation_id")
    private Invitation invitation;


    private boolean read = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}
