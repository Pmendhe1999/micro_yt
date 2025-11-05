package com.qc.QcService.repositories;

import com.qc.QcService.entities.DeliveryItemsMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryItemsMasterRepository extends JpaRepository<DeliveryItemsMaster, Long> {
    Page<DeliveryItemsMaster> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
