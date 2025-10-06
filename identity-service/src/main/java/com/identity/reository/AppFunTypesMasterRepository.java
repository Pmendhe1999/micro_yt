package com.identity.reository;

import com.identity.entity.AppFunTypesMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppFunTypesMasterRepository extends JpaRepository<AppFunTypesMaster, Long> {

    boolean existsByName(String name);

    Page<AppFunTypesMaster> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // ✅ Add this line to fix your issue
    Optional<AppFunTypesMaster> findByNameIgnoreCase(String name);
}
