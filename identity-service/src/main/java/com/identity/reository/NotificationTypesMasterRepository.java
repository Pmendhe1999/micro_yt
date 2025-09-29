package com.identity.reository;

import com.identity.entity.NotificationTypesMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypesMasterRepository extends JpaRepository<NotificationTypesMaster, Long> {

    boolean existsByName(String name);
    Page<NotificationTypesMaster> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
