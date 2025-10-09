package com.qc.QcService.repositories;

import com.qc.QcService.entities.ProductMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMasterRepository  extends JpaRepository<ProductMaster, Long> {

    boolean existsByName(String name);
    Page<ProductMaster> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
