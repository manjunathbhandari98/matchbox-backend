package com.quodex.matchbox.repository;

import com.quodex.matchbox.enums.InvitationStatus;
import com.quodex.matchbox.model.Invitation;
import com.quodex.matchbox.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, String> {
    boolean existsByInvitedUserAndStatus(User user, InvitationStatus status);

    Optional<Invitation> findByInviterAndInvitedUser(User inviter, User invitedUser);
    List<Invitation> findByInviterAndStatus(User sender, InvitationStatus status);

}
