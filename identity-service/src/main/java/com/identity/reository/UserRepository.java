package com.identity.reository;

import com.identity.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserCredential, Integer> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<UserCredential> findByEmail(String email);

    // Already have findById from JpaRepository
    Optional<UserCredential> findByUserId(Long userId);
}
