package com.qc.QcService.repositories;

import com.qc.QcService.entities.LabelScanMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelScanMasterRepository extends JpaRepository<LabelScanMaster, Long> {

    boolean existsByName(String name);

    Page<LabelScanMaster> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
