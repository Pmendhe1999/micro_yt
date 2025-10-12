package com.qc.QcService.repositories;

import com.qc.QcService.entities.QuantitativeCheckMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuantitativeCheckMasterRepository extends JpaRepository<QuantitativeCheckMaster, Long> {
    Page<QuantitativeCheckMaster> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
