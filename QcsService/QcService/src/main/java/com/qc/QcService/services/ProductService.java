package com.qc.QcService.services;

import com.qc.QcService.dto.ProductDTO;
import com.qc.QcService.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    Product saveProduct(ProductDTO dto, String token);

    Page<Product> getAllProducts(String search, Pageable pageable);

    Optional<Product> getProductById(Long id);

    Product updateProduct(Long id, ProductDTO dto, String token);

    Product deleteProduct(Long id, String token);
}
