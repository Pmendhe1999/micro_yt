package com.identity.reository;

import com.identity.entity.AuthTypeMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTypeMasterRepository  extends JpaRepository<AuthTypeMaster, Long> {
    boolean existsByName(String name);

    Page<AuthTypeMaster> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
