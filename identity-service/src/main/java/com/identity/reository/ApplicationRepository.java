package com.identity.reository;

import com.identity.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByApplicationName(String applicationName);
    Page<Application> findByApplicationNameContainingIgnoreCase(String name, Pageable pageable);
}
