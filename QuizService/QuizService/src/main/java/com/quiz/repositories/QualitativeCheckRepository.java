package com.quiz.repositories;

import com.quiz.entities.QualitativeCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QualitativeCheckRepository  extends JpaRepository<QualitativeCheck, Long> {

    @Query("SELECT q FROM QualitativeCheck q " +
            "LEFT JOIN q.qualitativeCheckMaster qm " +
            "LEFT JOIN q.product p " + // ✅ added join
            "WHERE (:description IS NULL OR LOWER(q.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:isScan IS NULL OR q.isScan = :isScan) " +
            "AND (:status IS NULL OR LOWER(q.status) LIKE LOWER(CONCAT('%', :status, '%'))) " +
            "AND (:value IS NULL OR LOWER(q.value) LIKE LOWER(CONCAT('%', :value, '%'))) " +
            "AND (:qualitativeCheckMasterIds IS NULL OR qm.id IN :qualitativeCheckMasterIds) " +
            "AND (:productIds IS NULL OR p.id IN :productIds)") // ✅ product filter
    Page<QualitativeCheck> searchQualitativeChecksAdvanced(
            @Param("description") String description,
            @Param("isScan") Boolean isScan,
            @Param("status") String status,
            @Param("value") String value,
            @Param("qualitativeCheckMasterIds") List<Long> qualitativeCheckMasterIds,
            @Param("productIds") List<Long> productIds,
            Pageable pageable);

}
