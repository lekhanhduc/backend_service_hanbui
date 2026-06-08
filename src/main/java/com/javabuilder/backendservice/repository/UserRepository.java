package com.javabuilder.backendservice.repository;

import com.javabuilder.backendservice.entity.User;
import com.javabuilder.backendservice.repository.projection.EmailDisplayNameOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<EmailDisplayNameOnly> findEmailDisplayNameOnlyByEmail(String email);

    @Query("SELECT u FROM User u WHERE (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:displayName IS NULL OR LOWER(u.displayName) LIKE LOWER(CONCAT('%', :displayName, '%')))")
    Page<User> searchUsers(String email, String displayName, Pageable pageable);
}
