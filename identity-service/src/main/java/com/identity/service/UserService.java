package com.identity.service;

import com.identity.dto.UserRegisterDto;
import com.identity.entity.UserCredential;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserCredential saveUser(UserRegisterDto dto, String token);

    Page<UserCredential> getAllUsers(String search, Pageable pageable);

    Optional<UserCredential> getUserById(Long id);

    UserCredential updateUser(Long id, UserRegisterDto dto, String token);

    UserCredential deleteUser(Long id, String token);
}
