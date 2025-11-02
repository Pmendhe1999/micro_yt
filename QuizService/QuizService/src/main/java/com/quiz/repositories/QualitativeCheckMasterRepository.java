package com.quiz.repositories;

import com.quiz.entities.QualitativeCheckMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualitativeCheckMasterRepository extends JpaRepository<QualitativeCheckMaster, Long> {

    @Query("SELECT DISTINCT qcm FROM QualitativeCheckMaster qcm " +
            "LEFT JOIN qcm.scanMaster sm " +
            "WHERE (:name IS NULL OR LOWER(qcm.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:description IS NULL OR LOWER(qcm.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:status IS NULL OR LOWER(qcm.status) LIKE LOWER(CONCAT('%', :status, '%'))) " +
            "AND (:checkStatus IS NULL OR qcm.checkStatus = :checkStatus) " +
            "AND (:scanMasterIds IS NULL OR sm.id IN :scanMasterIds)")
    Page<QualitativeCheckMaster> searchQualitativeCheckMastersAdvanced(
            @Param("name") String name,
            @Param("description") String description,
            @Param("status") String status,
            @Param("checkStatus") QualitativeCheckMaster.CheckStatus checkStatus,
            @Param("scanMasterIds") List<Long> scanMasterIds,
            Pageable pageable);
}
