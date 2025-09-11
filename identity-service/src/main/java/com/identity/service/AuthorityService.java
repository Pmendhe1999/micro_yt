package com.identity.service;

import com.identity.dto.AuthorityDTO;
import com.identity.entity.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorityService {
    Authority saveAuthority(AuthorityDTO authorityDTO, String token);

    Page<Authority> getAllAuthorities(String search, Pageable pageable);

    Optional<Authority> getAuthorityById(Long id);

    Authority updateAuthorityReturnEntity(Long id, AuthorityDTO updatedAuthorityDTO, String token);

    Authority deleteAuthorityReturnEntity(Long id, String token);
}
