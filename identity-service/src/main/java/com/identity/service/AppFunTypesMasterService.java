package com.identity.service;

import com.identity.dto.AppFunTypesMasterDTO;
import com.identity.entity.AppFunTypesMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AppFunTypesMasterService {

    AppFunTypesMaster save(AppFunTypesMasterDTO dto, String token);

    Page<AppFunTypesMaster> getAll(String search, Pageable pageable);

    Optional<AppFunTypesMaster> getById(Long id);

    AppFunTypesMaster update(Long id, AppFunTypesMasterDTO dto, String token);

    AppFunTypesMaster delete(Long id, String token);

    AppFunTypesMaster patchAppFunType(Long id, String key, Object value);

}
