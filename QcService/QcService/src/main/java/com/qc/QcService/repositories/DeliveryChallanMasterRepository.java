package com.qc.QcService.repositories;

import com.qc.QcService.entities.DeliveryChallanMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryChallanMasterRepository extends JpaRepository<DeliveryChallanMaster, Long> {
    Page<DeliveryChallanMaster> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
