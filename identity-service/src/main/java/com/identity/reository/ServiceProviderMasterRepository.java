package com.identity.reository;

import com.identity.entity.ServiceProviderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProviderMasterRepository extends JpaRepository<ServiceProviderMaster, Long> {
    boolean existsByName(String name);

    Page<ServiceProviderMaster> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
