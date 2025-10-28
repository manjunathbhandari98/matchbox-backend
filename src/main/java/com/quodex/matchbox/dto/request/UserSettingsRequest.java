package com.quodex.matchbox.dto.request;

import com.quodex.matchbox.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingsRequest {
    private Theme theme;
    private boolean emailNotifications;
    private boolean taskAssignmentNotifications;
    private boolean projectUpdateNotifications;
    private boolean commentsAndMentionNotifications;
    private boolean weeklySummary;
    private boolean twoFactorAuth;
}
