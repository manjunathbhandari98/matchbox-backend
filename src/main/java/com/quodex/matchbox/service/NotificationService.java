package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.response.NotificationResponse;
import com.quodex.matchbox.enums.NotificationType;
import com.quodex.matchbox.model.Invitation;
import com.quodex.matchbox.model.Notification;

import java.util.List;

public interface NotificationService {
    void sendNotification(
            String senderId,
            String receiverId,
            NotificationType type,
            String title,
            String message,
            String invitationId
    );
    List<NotificationResponse> getUserNotifications(String userId);

    void markAsRead(String notificationId);
    void deleteNotification(String id);

}
