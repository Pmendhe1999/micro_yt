package com.qc.QcService.repositories;

import com.qc.QcService.entities.QuantitativeCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuantitativeCheckRepository extends JpaRepository<QuantitativeCheck, Long> {
    Page<QuantitativeCheck> findByValueContainingIgnoreCase(String value, Pageable pageable);

}
