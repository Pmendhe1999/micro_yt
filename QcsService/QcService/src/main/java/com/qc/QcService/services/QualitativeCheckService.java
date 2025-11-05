package com.qc.QcService.services;

import com.qc.QcService.dto.QualitativeCheckDTO;
import com.qc.QcService.entities.QualitativeCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QualitativeCheckService {
    QualitativeCheck saveCheck(QualitativeCheckDTO dto, String token);

    Page<QualitativeCheck> getAllChecks(String search, Pageable pageable);

    Optional<QualitativeCheck> getCheckById(Long id);

    QualitativeCheck updateCheck(Long id, QualitativeCheckDTO dto, String token);

    QualitativeCheck deleteCheck(Long id, String token);
}
