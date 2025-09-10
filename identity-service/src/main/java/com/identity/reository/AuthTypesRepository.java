package com.identity.reository;

import com.identity.entity.AuthTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthTypesRepository extends JpaRepository<AuthTypes, Long> {

    boolean existsByAuthTypeName(String authTypeName);  // ✅ FIXED
    Optional<AuthTypes> findByAuthTypeName(String authTypeName);

    Page<AuthTypes> findByAuthTypeNameContainingIgnoreCase(String name, Pageable pageable);
}
