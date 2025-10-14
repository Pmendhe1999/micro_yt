package com.identity.reository;

import com.identity.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByName(String name);

    Page<Application> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Application> findByApplicationId(Long id);
}
