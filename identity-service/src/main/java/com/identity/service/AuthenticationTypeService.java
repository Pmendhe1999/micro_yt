package com.identity.service;

import com.identity.entity.AuthenticationType;

import java.util.List;
import java.util.Optional;

public interface AuthenticationTypeService {

    String saveAuthType(AuthenticationType authType, String token);
    List<AuthenticationType> getAllAuthTypes();
    Optional<AuthenticationType> getAuthTypeById(Long id);
    String updateAuthType(Long id, AuthenticationType updatedAuthType, String token);
    String deleteAuthType(Long id, String token);
}
