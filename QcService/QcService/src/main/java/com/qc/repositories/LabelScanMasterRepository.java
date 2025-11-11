package com.qc.repositories;

import com.qc.entities.LabelScanMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LabelScanMasterRepository extends JpaRepository<LabelScanMaster, Long> {

    @Query("SELECT l FROM LabelScanMaster l " +
            "WHERE (:name IS NULL OR LOWER(l.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:description IS NULL OR LOWER(l.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:scanType IS NULL OR LOWER(l.scanType) LIKE LOWER(CONCAT('%', :scanType, '%'))) " +
            "AND (:checkStatus IS NULL OR l.checkStatus = :checkStatus) " +
            "AND (:status IS NULL OR l.status = :status)")
    Page<LabelScanMaster> searchLabelScanMasters(
            @Param("name") String name,
            @Param("description") String description,
            @Param("scanType") String scanType,
            @Param("checkStatus") LabelScanMaster.CheckStatus checkStatus,
            @Param("status") Boolean status,
            Pageable pageable);
}
