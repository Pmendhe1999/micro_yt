package com.qc.QcService.services;

import com.qc.QcService.dto.QuantitativeCheckMasterDTO;
import com.qc.QcService.entities.QuantitativeCheckMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuantitativeCheckMasterService {
    QuantitativeCheckMaster saveCheck(QuantitativeCheckMasterDTO dto, String token);

    Page<QuantitativeCheckMaster> getAllChecks(String search, Pageable pageable);

    Optional<QuantitativeCheckMaster> getCheckById(Long id);

    QuantitativeCheckMaster updateCheck(Long id, QuantitativeCheckMasterDTO dto, String token);

    QuantitativeCheckMaster deleteCheck(Long id, String token);
}
