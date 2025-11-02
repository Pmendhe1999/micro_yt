package com.quiz.repositories;

import com.quiz.entities.DeliveryItemsMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryItemsMasterRepository extends JpaRepository<DeliveryItemsMaster, Long> {
    @Query("SELECT d FROM DeliveryItemsMaster d " +
            "LEFT JOIN d.challan c " +
            "WHERE (:batchNo IS NULL OR LOWER(d.batchNo) LIKE LOWER(CONCAT('%', :batchNo, '%'))) " +
            "AND (:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:productCode IS NULL OR LOWER(d.productCode) LIKE LOWER(CONCAT('%', :productCode, '%'))) " +
            "AND (:orderNo IS NULL OR LOWER(d.orderNo) LIKE LOWER(CONCAT('%', :orderNo, '%'))) " +
            "AND (:serialNo IS NULL OR LOWER(d.serialNo) LIKE LOWER(CONCAT('%', :serialNo, '%'))) " +
            "AND (:unit IS NULL OR LOWER(d.unit) LIKE LOWER(CONCAT('%', :unit, '%'))) " +
            "AND (:hsnCode IS NULL OR LOWER(d.hsnCode) LIKE LOWER(CONCAT('%', :hsnCode, '%'))) " +
            "AND (:challanIds IS NULL OR c.id IN :challanIds)")
    Page<DeliveryItemsMaster> searchDeliveryItemsAdvanced(
            @Param("batchNo") String batchNo,
            @Param("name") String name,
            @Param("productCode") String productCode,
            @Param("orderNo") String orderNo,
            @Param("serialNo") String serialNo,
            @Param("unit") String unit,
            @Param("hsnCode") String hsnCode,
            @Param("challanIds") List<Long> challanIds,
            Pageable pageable
    );
}
