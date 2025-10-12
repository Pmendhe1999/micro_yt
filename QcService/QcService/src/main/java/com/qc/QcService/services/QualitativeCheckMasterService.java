package com.qc.QcService.services;

import com.qc.QcService.dto.QualitativeCheckMasterDTO;
import com.qc.QcService.entities.QualitativeCheckMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QualitativeCheckMasterService {


    QualitativeCheckMaster saveCheck(QualitativeCheckMasterDTO dto, String token);

    Page<QualitativeCheckMaster> getAllChecks(String search, Pageable pageable);

    Optional<QualitativeCheckMaster> getCheckById(Long id);

    QualitativeCheckMaster updateCheck(Long id, QualitativeCheckMasterDTO dto, String token);

    QualitativeCheckMaster deleteCheck(Long id, String token);
}
