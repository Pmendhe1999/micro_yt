package com.identity.service;

import com.identity.dto.AppFunctionDTO;
import com.identity.entity.AppFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AppFunctionService {
    AppFunction saveAppFunction(AppFunctionDTO dto, String token);
    Page<AppFunction> getAllAppFunctions(String search, Pageable pageable);
    Optional<AppFunction> getAppFunctionById(Long id);
    AppFunction updateAppFunction(Long id, AppFunctionDTO dto, String token);
    AppFunction deleteAppFunction(Long id, String token);
}
