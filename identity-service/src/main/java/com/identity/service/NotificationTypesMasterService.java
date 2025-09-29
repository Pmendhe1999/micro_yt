package com.identity.service;

import com.identity.dto.NotificationTypesMasterDTO;
import com.identity.entity.NotificationTypesMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationTypesMasterService {
    NotificationTypesMaster saveNotificationType(NotificationTypesMasterDTO dto, String token);
    Page<NotificationTypesMaster> getAllNotificationTypes(String search, Pageable pageable);
    Optional<NotificationTypesMaster> getNotificationTypeById(Long id);
    NotificationTypesMaster updateNotificationTypeReturnEntity(Long id, NotificationTypesMasterDTO dto, String token);
    NotificationTypesMaster deleteNotificationTypeReturnEntity(Long id, String token);
}
