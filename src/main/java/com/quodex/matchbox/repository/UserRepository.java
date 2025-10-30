package com.quodex.matchbox.repository;

import com.quodex.matchbox.dto.response.MemberResponse;
import com.quodex.matchbox.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    Optional<User> findByEmail(String username);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<MemberResponse> searchUsers(@Param("query") String query);

    List<User> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String query, String query1);
}
