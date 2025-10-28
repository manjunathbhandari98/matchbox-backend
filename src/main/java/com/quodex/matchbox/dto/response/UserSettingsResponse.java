package com.quodex.matchbox.dto.response;

import com.quodex.matchbox.enums.Theme;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSettingsResponse {
    private boolean emailNotifications;
    private boolean taskAssignmentNotifications;
    private boolean projectUpdateNotifications;
    private boolean commentsAndMentionNotifications;
    private boolean weeklySummary;
    private Theme theme;
    private boolean twoFactorAuth;
}
