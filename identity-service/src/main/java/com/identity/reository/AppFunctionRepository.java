package com.identity.reository;

import com.identity.entity.AppFunction;
import com.identity.entity.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppFunctionRepository extends JpaRepository<AppFunction, Long> {
    Page<AppFunction> findByNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByName(String name);
}
