package com.qc.QcService.services;

import com.qc.QcService.dto.ProductMasterDescriptionDTO;
import com.qc.QcService.entities.ProductMasterDescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductMasterDescriptionService {
    ProductMasterDescription saveDescription(ProductMasterDescriptionDTO dto, String token);
    Page<ProductMasterDescription> getAllDescriptions(String search, Pageable pageable);
    Optional<ProductMasterDescription> getDescriptionById(Long id);
    ProductMasterDescription updateDescription(Long id, ProductMasterDescriptionDTO dto, String token);
    ProductMasterDescription deleteDescription(Long id, String token);
}
