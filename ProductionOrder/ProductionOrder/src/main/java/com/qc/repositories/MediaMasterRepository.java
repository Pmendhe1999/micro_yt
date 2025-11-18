package com.qc.repositories;

import com.qc.entities.MediaMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MediaMasterRepository extends JpaRepository<MediaMaster, Long> {

    Optional<MediaMaster> findByProductMasterId(Long productMasterId);


    // Fetch all MediaMaster records for a given productMasterId
    List<MediaMaster> findAllByProductMasterId(Long productMasterId);
}
