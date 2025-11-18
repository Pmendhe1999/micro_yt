package com.qc.repositories;

import com.qc.entities.ProductDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDescriptionRepository extends JpaRepository<ProductDescription, Long> {
}
