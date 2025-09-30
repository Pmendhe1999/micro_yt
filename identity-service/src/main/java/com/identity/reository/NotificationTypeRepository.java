package com.identity.reository;

import com.identity.entity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {

    Page<NotificationType> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByName(String name);

    @Query("SELECT nt FROM NotificationType nt " +
            "WHERE (:search IS NULL OR LOWER(nt.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:appFunctionIds IS NULL OR nt.appFunction.id IN :appFunctionIds) " +
            "AND (:notificationTechIds IS NULL OR nt.notificationTechDetails.id IN :notificationTechIds) " +
            "AND (:notificationTypeMasterIds IS NULL OR nt.notificationTypesMaster.id IN :notificationTypeMasterIds)")
    Page<NotificationType> findByFilters(@Param("search") String search,
                                         @Param("appFunctionIds") List<Long> appFunctionIds,
                                         @Param("notificationTechIds") List<Long> notificationTechIds,
                                         @Param("notificationTypeMasterIds") List<Long> notificationTypeMasterIds,
                                         Pageable pageable);
}
