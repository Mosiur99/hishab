package com.project.hishab.repository;

import com.project.hishab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByVerificationToken(String verificationToken);
    Optional<User> findByResetToken(String resetToken);
    boolean existsByEmail(String email);
}
