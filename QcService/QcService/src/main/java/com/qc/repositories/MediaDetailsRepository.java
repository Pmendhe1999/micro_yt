package com.qc.repositories;

import com.qc.entities.MediaDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaDetailsRepository  extends JpaRepository<MediaDetails, Long> {
}
