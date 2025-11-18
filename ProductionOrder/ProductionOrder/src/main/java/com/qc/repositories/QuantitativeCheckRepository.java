package com.qc.repositories;

import com.qc.entities.QuantitativeCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuantitativeCheckRepository extends JpaRepository<QuantitativeCheck, Long> {
    @Query("SELECT q FROM QuantitativeCheck q " +
            "LEFT JOIN q.quantitativeCheckMaster qm " +
            "LEFT JOIN q.product p " +
            "WHERE (:description IS NULL OR LOWER(q.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:isScan IS NULL OR q.isScan = :isScan) " +
            "AND (:status IS NULL OR LOWER(q.status) LIKE LOWER(CONCAT('%', :status, '%'))) " +
            "AND (:value IS NULL OR LOWER(q.value) LIKE LOWER(CONCAT('%', :value, '%'))) " +
            "AND (:quantitativeCheckMasterIds IS NULL OR qm.id IN :quantitativeCheckMasterIds) " +
            "AND (:productIds IS NULL OR p.id IN :productIds)")
    Page<QuantitativeCheck> searchQuantitativeChecksAdvanced(
            @Param("description") String description,
            @Param("isScan") Boolean isScan,
            @Param("status") String status,
            @Param("value") String value,
            @Param("quantitativeCheckMasterIds") List<Long> quantitativeCheckMasterIds,
            @Param("productIds") List<Long> productIds,
            Pageable pageable);
}
