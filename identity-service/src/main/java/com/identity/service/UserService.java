package com.identity.service;

import com.identity.dto.ChangePasswordRequest;
import com.identity.dto.UserRegisterDto;
import com.identity.entity.UserCredential;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserCredential saveUser(UserRegisterDto dto);
    ResponseEntity<?> changePassword(ChangePasswordRequest request);
    /**
     * Fetch all users with optional filters and pagination.
     *
     * @param username       Filter by username (optional)
     * @param email          Filter by email (optional)
     * @param mobileNumber   Filter by mobile number (optional)
     * @param country        Filter by country (optional)
     * @param firstName      Filter by first name (optional)
     * @param lastName       Filter by last name (optional)
     * @param applicationIds Filter by multiple application IDs (optional)
     * @param authorityIds   Filter by multiple authority IDs (optional)
     * @param pageable       Pagination and sorting information
     * @return Page of UserCredential matching the filters
     */
    Page<UserCredential> getAllUsersWithFilters(
            String username,
            String email,
            String mobileNumber,
            String country,
            String firstName,
            String lastName,
            List<Long> applicationIds,
            List<Long> authorityIds,
            Boolean activationKey,
            Boolean activated,
            Pageable pageable
    );


    Optional<UserCredential> getUserById(Long id);

    UserCredential updateUser(Long id, UserRegisterDto dto, String token);

    UserCredential deleteUser(Long id, String token);

    UserCredential activateUser(Long id, Boolean activated, String token);

    UserCredential patchUser(Long id, String key, Object value, String token);

}
