package com.identity.service;

import com.identity.dto.ApplicationDTO;
import com.identity.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ApplicationService {
    Application saveApplication(ApplicationDTO applicationDTO, String token);
    Page<Application> getAllApplications(String search, Pageable pageable);
    Optional<Application> getApplicationById(Long applicationId);
    Application updateApplicationReturnEntity(Long applicationId, ApplicationDTO updatedApplicationDTO, String token);
    Application deleteApplicationReturnEntity(Long applicationId, String token);
    Application patchApplication(Long id, String key, Object value);
}
