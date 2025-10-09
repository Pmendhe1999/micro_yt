package com.qc.QcService.repositories;

import com.qc.QcService.entities.ProductMasterDescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMasterDescriptionRepository  extends JpaRepository<ProductMasterDescription, Long> {
    Page<ProductMasterDescription> findByDescriptionsContainingIgnoreCase(String descriptions, Pageable pageable);

}
