package com.identity.reository;

import com.identity.entity.UserCredential;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserCredential, Integer> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<UserCredential> findByEmail(String email);
    Optional<UserCredential> findByUsername(String username);
    // Already have findById from JpaRepository
    Optional<UserCredential> findByUserId(Long userId);

    @Query("SELECT u FROM UserCredential u " +
            "WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "   OR LOWER(u.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "   OR LOWER(u.country) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "   OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "   OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<UserCredential> searchUsers(@Param("search") String search, Pageable pageable);

    @Query("SELECT DISTINCT u FROM UserCredential u " +
            "LEFT JOIN u.applications a " +
            "LEFT JOIN u.authorities auth " +
            "WHERE (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) " +
            "AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:mobileNumber IS NULL OR u.mobileNumber LIKE CONCAT('%', :mobileNumber, '%')) " +
            "AND (:country IS NULL OR LOWER(u.country) LIKE LOWER(CONCAT('%', :country, '%'))) " +
            "AND (:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) " +
            "AND (:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) " +
            "AND (:applicationIds IS NULL OR a.applicationId IN :applicationIds) " +
            "AND (:authorityIds IS NULL OR auth.authorityId IN :authorityIds)")
    Page<UserCredential> searchUsersAdvanced(
            @Param("username") String username,
            @Param("email") String email,
            @Param("mobileNumber") String mobileNumber,
            @Param("country") String country,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("applicationIds") List<Long> applicationIds,
            @Param("authorityIds") List<Long> authorityIds,
            Pageable pageable);
}
