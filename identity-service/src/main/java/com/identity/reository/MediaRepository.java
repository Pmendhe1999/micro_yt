package com.identity.reository;

import com.identity.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface MediaRepository extends JpaRepository<Media, Long> {
    boolean existsByName(String name);

    Page<Media> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
