package com.qc.QcService.repositories;

import com.qc.QcService.entities.ProductDescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDescriptionRepository extends JpaRepository<ProductDescription, Long> {
    Page<ProductDescription> findByDescriptionsContainingIgnoreCase(String descriptions, Pageable pageable);

}
