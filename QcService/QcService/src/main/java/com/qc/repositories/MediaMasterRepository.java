package com.qc.repositories;

import com.qc.entities.MediaMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaMasterRepository extends JpaRepository<MediaMaster, Long> {

    Optional<MediaMaster> findByProductId(Long productId);
}
