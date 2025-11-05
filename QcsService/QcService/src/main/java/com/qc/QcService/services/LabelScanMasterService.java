package com.qc.QcService.services;

import com.qc.QcService.dto.LabelScanMasterDTO;
import com.qc.QcService.entities.LabelScanMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LabelScanMasterService {

    LabelScanMaster saveLabel(LabelScanMasterDTO dto, String token);
    Page<LabelScanMaster> getAllLabels(String search, Pageable pageable);
    Optional<LabelScanMaster> getLabelById(Long id);
    LabelScanMaster updateLabel(Long id, LabelScanMasterDTO dto, String token);
    LabelScanMaster deleteLabel(Long id, String token);
}
