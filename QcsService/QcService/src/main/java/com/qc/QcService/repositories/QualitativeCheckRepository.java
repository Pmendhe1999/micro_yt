package com.qc.QcService.repositories;

import com.qc.QcService.entities.QualitativeCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualitativeCheckRepository  extends JpaRepository<QualitativeCheck, Long> {
    Page<QualitativeCheck> findByValueContainingIgnoreCase(String value, Pageable pageable);

}
