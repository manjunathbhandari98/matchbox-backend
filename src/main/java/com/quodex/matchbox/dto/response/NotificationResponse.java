package com.quodex.matchbox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private String id;
    private String type;
    private String message;
    private LocalDateTime createdAt;
    private String senderId;
    private String senderName;
    private String senderAvatar;
    private String invitationId;
    private String receiverId;
}
