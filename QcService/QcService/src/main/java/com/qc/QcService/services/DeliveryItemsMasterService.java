package com.qc.QcService.services;

import com.qc.QcService.dto.DeliveryItemsMasterDTO;
import com.qc.QcService.entities.DeliveryItemsMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeliveryItemsMasterService {

    DeliveryItemsMaster saveItem(DeliveryItemsMasterDTO dto, String token);

    Page<DeliveryItemsMaster> getAllItems(String search, Pageable pageable);

    Optional<DeliveryItemsMaster> getItemById(Long id);

    DeliveryItemsMaster updateItem(Long id, DeliveryItemsMasterDTO dto, String token);

    DeliveryItemsMaster deleteItem(Long id, String token);
}
