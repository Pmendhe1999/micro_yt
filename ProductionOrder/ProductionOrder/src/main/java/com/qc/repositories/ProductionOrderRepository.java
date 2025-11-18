package com.qc.repositories;

import com.qc.entities.ProductionOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {
    @Query("SELECT p FROM ProductionOrder p " +
            "WHERE (:productionOrderNo IS NULL OR LOWER(p.productionOrderNo) LIKE LOWER(CONCAT('%', :productionOrderNo, '%'))) " +
            "AND (:batchNo IS NULL OR LOWER(p.batchNo) LIKE LOWER(CONCAT('%', :batchNo, '%'))) " +
            "AND (:currentWorkCenter IS NULL OR LOWER(p.currentWorkCenter) LIKE LOWER(CONCAT('%', :currentWorkCenter, '%'))) " +
            "AND (:activityNumber IS NULL OR LOWER(p.activityNumber) LIKE LOWER(CONCAT('%', :activityNumber, '%'))) " +
            "AND (:operation IS NULL OR LOWER(p.operation) LIKE LOWER(CONCAT('%', :operation, '%'))) " +
            "AND (:priority IS NULL OR p.priority = :priority)")
    Page<ProductionOrder> search(
            @Param("productionOrderNo") String productionOrderNo,
            @Param("batchNo") String batchNo,
            @Param("currentWorkCenter") String currentWorkCenter,
            @Param("activityNumber") String activityNumber,
            @Param("operation") String operation,
            @Param("priority") Long priority,
            Pageable pageable);

    @Query("SELECT DISTINCT p.currentWorkCenter FROM ProductionOrder p WHERE p.currentWorkCenter IS NOT NULL")
    List<String> findDistinctCurrentWorkCenters();
}
