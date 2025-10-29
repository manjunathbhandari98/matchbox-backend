package com.quodex.matchbox.service;

import com.quodex.matchbox.dto.response.NotificationResponse;
import com.quodex.matchbox.enums.NotificationType;
import com.quodex.matchbox.model.Invitation;
import com.quodex.matchbox.model.Notification;
import com.quodex.matchbox.model.User;
import com.quodex.matchbox.repository.InvitationRepository;
import com.quodex.matchbox.repository.NotificationRepository;
import com.quodex.matchbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;

    //  Create new notification
    @Override
    public void sendNotification(
            String senderId,
            String receiverId,
            NotificationType type,
            String title,
            String message,
            String invitationId
    ) {
        User sender = (senderId != null)
                ? userRepository.findById(senderId).orElse(null)
                : null;

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Invitation invitation = null;
        if (invitationId != null) {
            invitation = invitationRepository.findById(invitationId)
                    .orElse(null);
        }

        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .type(type)
                .title(title)
                .message(message)
                .invitation(invitation) // ✅ link invitation if exists
                .build();

        notificationRepository.save(notification);
    }


    // Get all notifications for a user
    @Override
    public List<NotificationResponse> getUserNotifications(String userId) {
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Notification> notifications = notificationRepository.findByReceiverOrderByCreatedAtDesc(receiver);

        return notifications.stream().map(notification -> {
            User sender = notification.getSender();
            return new NotificationResponse(
                    notification.getId(),
                    notification.getType().name(),
                    notification.getMessage(),
                    notification.getCreatedAt(),
                    sender != null ? sender.getId() : null,
                    sender != null ? sender.getFullName() : null,
                    sender != null ? sender.getAvatar() : null,
                    notification.getInvitation() != null ? notification.getInvitation().getId() : null,
                    receiver.getId()
            );
        }).collect(Collectors.toList());
    }

    // Mark as read
    @Override
    public void markAsRead(String notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setRead(true);
        notificationRepository.save(n);
    }

    // Delete or clear notification
    @Override
    public void deleteNotification(String id) {
        notificationRepository.deleteById(id);
    }
}
