package com.qc.QcService.services;

import com.qc.QcService.dto.QuantitativeCheckDTO;
import com.qc.QcService.entities.QuantitativeCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuantitativeCheckService {
    QuantitativeCheck saveCheck(QuantitativeCheckDTO dto, String token);

    Page<QuantitativeCheck> getAllChecks(String search, Pageable pageable);

    Optional<QuantitativeCheck> getCheckById(Long id);

    QuantitativeCheck updateCheck(Long id, QuantitativeCheckDTO dto, String token);

    QuantitativeCheck deleteCheck(Long id, String token);
}
