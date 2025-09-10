package com.identity.service;

import com.identity.entity.Authority;

import java.util.List;
import java.util.Optional;

public interface AuthorityService {
    String saveAuthority(Authority authority, String token);

    List<Authority> getAllAuthorities();

    Optional<Authority> getAuthorityById(Long id);

    String updateAuthority(Long id, Authority updatedAuthority, String token);

    String deleteAuthority(Long id, String token);
}
