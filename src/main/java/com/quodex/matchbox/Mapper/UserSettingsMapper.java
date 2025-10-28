package com.quodex.matchbox.Mapper;

import com.quodex.matchbox.dto.response.UserSettingsResponse;
import com.quodex.matchbox.enums.Theme;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.model.UserSettings;

public class UserSettingsMapper {

    public static UserSettings toDefaultSettings(User user) {
        return UserSettings.builder()
                .user(user)
                .theme(Theme.LIGHT)
                .emailNotifications(true)
                .taskAssignmentNotifications(true)
                .projectUpdateNotifications(true)
                .commentsAndMentionNotifications(true)
                .weeklySummary(false)
                .twoFactorAuth(false)
                .build();
    }

    // Fixed: Changed return type from UserSettings to UserSettingsResponse
    public static UserSettingsResponse toResponse(UserSettings settings) {
        return UserSettingsResponse.builder()
                .theme(settings.getTheme())
                .emailNotifications(settings.isEmailNotifications())
                .taskAssignmentNotifications(settings.isTaskAssignmentNotifications())
                .projectUpdateNotifications(settings.isProjectUpdateNotifications())
                .commentsAndMentionNotifications(settings.isCommentsAndMentionNotifications())
                .weeklySummary(settings.isWeeklySummary())
                .twoFactorAuth(settings.isTwoFactorAuth())
                .build();
    }
}