package com.qc.QcService.services;

import com.qc.QcService.dto.ProductDescriptionDTO;
import com.qc.QcService.entities.ProductDescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductDescriptionService {
    ProductDescription saveDescription(ProductDescriptionDTO dto, String token);

    Page<ProductDescription> getAllDescriptions(String search, Pageable pageable);

    Optional<ProductDescription> getDescriptionById(Long id);

    ProductDescription updateDescription(Long id, ProductDescriptionDTO dto, String token);

    ProductDescription deleteDescription(Long id, String token);
}
