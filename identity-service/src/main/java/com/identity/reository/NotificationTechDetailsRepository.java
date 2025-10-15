package com.identity.reository;

import com.identity.entity.NotificationTechDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationTechDetailsRepository extends JpaRepository<NotificationTechDetails, Long> {
    Page<NotificationTechDetails> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<NotificationTechDetails> findByServiceProviderMaster_Id(Long serviceProviderMasterId);

    @Query("SELECT DISTINCT n FROM NotificationTechDetails n " +
            "LEFT JOIN n.serviceProviderMaster sp " +
            "WHERE (:name IS NULL OR LOWER(n.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:description IS NULL OR LOWER(n.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:notes IS NULL OR LOWER(n.notes) LIKE LOWER(CONCAT('%', :notes, '%'))) " +
            "AND (:status IS NULL OR n.status = :status) " +
            "AND (:serviceProviderIds IS NULL OR sp.id IN :serviceProviderIds)")
    Page<NotificationTechDetails> searchNotificationTechDetails(
            @Param("name") String name,
            @Param("description") String description,
            @Param("notes") String notes,
            @Param("status") Boolean status,
            @Param("serviceProviderIds") List<Long> serviceProviderIds,
            Pageable pageable
    );
}
