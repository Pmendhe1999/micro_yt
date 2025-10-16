package com.identity.service;

import com.identity.dto.AuthTypeDTO;
import com.identity.entity.AuthType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthTypeService {
    AuthType saveAuthType(AuthTypeDTO dto, String token);
    Page<AuthType> getAllAuthTypes(String name,String search, List<Long> appFuncIds, List<Long> authTypeMasterIds, Pageable pageable);
    Optional<AuthType> getAuthTypeById(Long id);
    AuthType updateAuthType(Long id, AuthTypeDTO dto, String token);
    AuthType deleteAuthType(Long id, String token);

    AuthType patchAuthType(Long id, String key, Object value);

}
