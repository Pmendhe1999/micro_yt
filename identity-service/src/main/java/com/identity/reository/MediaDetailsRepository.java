package com.identity.reository;

import com.identity.entity.MediaDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  MediaDetailsRepository extends JpaRepository<MediaDetails, Long> {

    Page<MediaDetails> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<MediaDetails> findByUser_UserId(Long userId);

    List<MediaDetails> findByMedia_Id(Long mediaId);
}
