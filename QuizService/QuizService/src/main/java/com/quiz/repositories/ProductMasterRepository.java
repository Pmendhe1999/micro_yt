package com.quiz.repositories;

import com.quiz.entities.ProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {
}
