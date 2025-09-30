package com.identity.reository;

import com.identity.entity.NotificationTechDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationTechDetailsRepository extends JpaRepository<NotificationTechDetails, Long> {
    Page<NotificationTechDetails> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<NotificationTechDetails> findByServiceProviderMaster_Id(Long serviceProviderMasterId);
}
