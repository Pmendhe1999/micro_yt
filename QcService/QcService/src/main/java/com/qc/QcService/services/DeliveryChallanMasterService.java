package com.qc.QcService.services;

import com.qc.QcService.dto.DeliveryChallanMasterDTO;
import com.qc.QcService.entities.DeliveryChallanMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeliveryChallanMasterService {

    DeliveryChallanMaster saveMaster(DeliveryChallanMasterDTO dto, String token);

    Page<DeliveryChallanMaster> getAllMasters(String search, Pageable pageable);

    Optional<DeliveryChallanMaster> getMasterById(Long id);

    DeliveryChallanMaster updateMaster(Long id, DeliveryChallanMasterDTO dto, String token);

    DeliveryChallanMaster deleteMaster(Long id, String token);
}
