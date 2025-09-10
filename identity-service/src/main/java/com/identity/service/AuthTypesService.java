package com.identity.service;

import com.identity.dto.AuthTypeDTO;
import com.identity.entity.AuthTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthTypesService {
    AuthTypes saveAuthType(AuthTypeDTO authTypeDTO, String token);

    Page<AuthTypes> getAllAuthTypes(String search, Pageable pageable);

    Optional<AuthTypes> getAuthTypeById(Long id);

    // Interface
    AuthTypes updateAuthTypeReturnEntity(Long id, AuthTypeDTO updatedAuthTypeDTO, String token);

    AuthTypes deleteAuthTypeReturnEntity(Long id, String token);
}
