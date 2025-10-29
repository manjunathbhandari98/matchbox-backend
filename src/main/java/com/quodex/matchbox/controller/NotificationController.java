package com.quodex.matchbox.controller;
import com.quodex.matchbox.dto.response.NotificationResponse;
import com.quodex.matchbox.enums.NotificationType;
import com.quodex.matchbox.model.Notification;
import com.quodex.matchbox.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 🔹 Get notifications for a user
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@RequestParam String userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    // 🔹 Mark as read
    @PostMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }

    // 🔹 Delete notification
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notification deleted");
    }

    // 🔹 Send new notification manually (for testing)
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @RequestParam(required = false) String senderId,
            @RequestParam String receiverId,
            @RequestParam NotificationType type,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(required = false) String invitationId
    ) {
        notificationService.sendNotification(senderId, receiverId, type, title, message, invitationId);
        return ResponseEntity.ok("Notification sent successfully");
    }

}

