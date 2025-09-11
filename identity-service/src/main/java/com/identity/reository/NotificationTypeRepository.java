package com.identity.reository;

import com.identity.entity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {

    boolean existsByNotificationName(String notificationName);

    Page<NotificationType> findByNotificationNameContainingIgnoreCase(String name, Pageable pageable);
}
