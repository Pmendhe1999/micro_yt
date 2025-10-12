package com.qc.QcService.services;

import com.qc.QcService.dto.DeliveryItemsDTO;
import com.qc.QcService.entities.DeliveryItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeliveryItemsService {

    DeliveryItems saveItem(DeliveryItemsDTO dto, String token);
    Page<DeliveryItems> getAllItems(String search, Pageable pageable);
    Optional<DeliveryItems> getItemById(Long id);
    DeliveryItems updateItem(Long id, DeliveryItemsDTO dto, String token);
    DeliveryItems deleteItem(Long id, String token);
}
