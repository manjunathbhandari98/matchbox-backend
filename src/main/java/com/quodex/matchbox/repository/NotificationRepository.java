package com.quodex.matchbox.repository;

import com.quodex.matchbox.model.Notification;
import com.quodex.matchbox.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByReceiverOrderByCreatedAtDesc(User receiver);
}
