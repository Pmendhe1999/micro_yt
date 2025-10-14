package com.identity.service;

import com.identity.dto.AuthTypeMasterDTO;
import com.identity.entity.AuthTypeMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AuthTypeMasterService {
    AuthTypeMaster save(AuthTypeMasterDTO dto, String token);

    Page<AuthTypeMaster> getAll(String search, Pageable pageable);

    Optional<AuthTypeMaster> getById(Long id);

    AuthTypeMaster update(Long id, AuthTypeMasterDTO dto, String token);

    AuthTypeMaster delete(Long id, String token);

    AuthTypeMaster patchAuthTypeMaster(Long id, String key, Object value);

}
