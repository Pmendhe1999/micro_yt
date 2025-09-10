package com.identity.service;

import com.identity.entity.AuthTypes;

import java.util.List;
import java.util.Optional;

public interface AuthTypesService {
    String saveAuthType(AuthTypes authType, String token);

    List<AuthTypes> getAllAuthTypes();

    Optional<AuthTypes> getAuthTypeById(Long id);

    String updateAuthType(Long id, AuthTypes updatedAuthType, String token);

    String deleteAuthType(Long id, String token);
}
