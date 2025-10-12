package com.qc.QcService.repositories;

import com.qc.QcService.entities.DeliveryChallan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryChallanRepository extends JpaRepository<DeliveryChallan, Long> {
    Page<DeliveryChallan> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
