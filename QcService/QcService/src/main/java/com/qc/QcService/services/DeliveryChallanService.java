package com.qc.QcService.services;

import com.qc.QcService.dto.DeliveryChallanDTO;
import com.qc.QcService.entities.DeliveryChallan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeliveryChallanService {

    DeliveryChallan saveChallan(DeliveryChallanDTO dto, String token);

    Page<DeliveryChallan> getAllChallans(String search, Pageable pageable);

    Optional<DeliveryChallan> getChallanById(Long id);

    DeliveryChallan updateChallan(Long id, DeliveryChallanDTO dto, String token);

    DeliveryChallan deleteChallan(Long id, String token);
}
