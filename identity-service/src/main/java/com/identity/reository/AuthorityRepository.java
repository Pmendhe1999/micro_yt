package com.identity.reository;

import com.identity.entity.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    boolean existsByName(String name);
    Optional<Authority> findByName(String name);
    Page<Authority> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
