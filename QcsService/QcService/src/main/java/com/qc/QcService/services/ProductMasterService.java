package com.qc.QcService.services;

import com.qc.QcService.dto.ProductMasterDTO;
import com.qc.QcService.entities.ProductMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductMasterService {

    ProductMaster saveProduct(ProductMasterDTO dto, String token);
    Page<ProductMaster> getAllProducts(String search, Pageable pageable);
    Optional<ProductMaster> getProductById(Long id);
    ProductMaster updateProduct(Long id, ProductMasterDTO dto, String token);
    ProductMaster deleteProduct(Long id, String token);
}
