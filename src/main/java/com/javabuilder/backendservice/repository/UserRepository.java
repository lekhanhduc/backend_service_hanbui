package com.javabuilder.backendservice.repository;

import com.javabuilder.backendservice.entity.User;
import com.javabuilder.backendservice.repository.projection.EmailDisplayNameOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<EmailDisplayNameOnly> findEmailDisplayNameOnlyByEmail(String email);

    @Query("SELECT u FROM User u LEFT " +
            "JOIN FETCH u.userHasRoles uhr " +
            "LEFT JOIN FETCH uhr.role WHERE u.id = :id")
    Optional<User> findByIdWithRoles(String id);
}
