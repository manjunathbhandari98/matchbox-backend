package com.quodex.matchbox.model;

import com.quodex.matchbox.enums.Theme;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_settings")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    Notifications Settings
    private boolean emailNotifications = true;
    private boolean taskAssignmentNotifications = true;
    private boolean projectUpdateNotifications = true;
    private boolean commentsAndMentionNotifications = true;
    private boolean weeklySummary = false;

//    Appearance
    private Theme theme = Theme.LIGHT;

//    Security
    private boolean twoFactorAuth = false;

}
