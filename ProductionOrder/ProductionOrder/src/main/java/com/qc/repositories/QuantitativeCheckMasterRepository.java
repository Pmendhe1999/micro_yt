package com.qc.repositories;

import com.qc.entities.QuantitativeCheckMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuantitativeCheckMasterRepository extends JpaRepository<QuantitativeCheckMaster, Long> {

    @Query("SELECT q FROM QuantitativeCheckMaster q " +
            "LEFT JOIN q.scanMaster s " +
            "WHERE (:name IS NULL OR LOWER(q.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:description IS NULL OR LOWER(q.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:checkStatus IS NULL OR q.checkStatus = :checkStatus) " +
            "AND (:status IS NULL OR LOWER(q.status) LIKE LOWER(CONCAT('%', :status, '%'))) " +
            "AND (:scanMasterIds IS NULL OR s.id IN :scanMasterIds)")
    Page<QuantitativeCheckMaster> searchQuantitativeCheckMasterAdvanced(
            @Param("name") String name,
            @Param("description") String description,
            @Param("checkStatus") QuantitativeCheckMaster.CheckStatus checkStatus,
            @Param("status") String status,
            @Param("scanMasterIds") List<Long> scanMasterIds,
            Pageable pageable);

    @Query("SELECT q FROM QuantitativeCheckMaster q WHERE q.scanMaster.id = :scanMasterId")
    List<QuantitativeCheckMaster> findByScanMasterId(@Param("scanMasterId") Long scanMasterId);
}
