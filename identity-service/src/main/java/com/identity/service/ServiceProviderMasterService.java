package com.identity.service;

import com.identity.dto.ServiceProviderMasterDTO;
import com.identity.entity.ServiceProviderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ServiceProviderMasterService {
    ServiceProviderMaster saveServiceProvider(ServiceProviderMasterDTO dto, String token);
    Page<ServiceProviderMaster> getAllServiceProviders(String search, Pageable pageable);
    Optional<ServiceProviderMaster> getServiceProviderById(Long id);
    ServiceProviderMaster updateServiceProviderReturnEntity(Long id, ServiceProviderMasterDTO dto, String token);
    ServiceProviderMaster deleteServiceProviderReturnEntity(Long id, String token);
}
