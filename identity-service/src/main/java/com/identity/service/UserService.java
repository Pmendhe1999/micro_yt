package com.identity.service;

import com.identity.dto.UserRegisterDto;
import com.identity.entity.UserCredential;

import java.util.List;
import java.util.Optional;

public interface UserService {

    String saveUser(UserRegisterDto dto, String token);

    List<UserCredential> getAllUsers();

    Optional<UserCredential> getUserById(int id);

    String updateUser(int id, UserRegisterDto dto, String token);

    String deleteUser(int id, String token);

}
