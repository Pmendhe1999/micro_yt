package com.identity.service;

import com.identity.dto.NotificationTechDetailsDTO;
import com.identity.entity.NotificationTechDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationTechDetailsService {
    NotificationTechDetails save(NotificationTechDetailsDTO dto, String token);
    Page<NotificationTechDetails> getAllNotificationTechDetailsWithFilters(
            String name,
            String description,
            String notes,
            Boolean status,
            List<Long> serviceProviderIds,
            Pageable pageable
    );    Optional<NotificationTechDetails> getById(Long id);
    NotificationTechDetails updateReturnEntity(Long id, NotificationTechDetailsDTO dto, String token);
    NotificationTechDetails deleteReturnEntity(Long id, String token);
    List<NotificationTechDetails> getByServiceProviderMasterId(Long serviceProviderMasterId);
    NotificationTechDetails patchNotificationTechDetails(Long id, String key, Object value);

}
