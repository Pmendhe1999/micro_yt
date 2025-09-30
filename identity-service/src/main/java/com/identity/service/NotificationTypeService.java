package com.identity.service;

import com.identity.dto.NotificationTypeDTO;
import com.identity.entity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationTypeService {
    NotificationType saveNotificationType(NotificationTypeDTO dto, String token);
    Page<NotificationType> getAllNotificationTypes(String search,
                                                   List<Long> appFunctionIds,
                                                   List<Long> notificationTechIds,
                                                   List<Long> notificationTypeMasterIds,
                                                   Pageable pageable);
    Optional<NotificationType> getNotificationTypeById(Long id);
    NotificationType updateNotificationType(Long id, NotificationTypeDTO dto, String token);
    NotificationType deleteNotificationType(Long id, String token);
}
