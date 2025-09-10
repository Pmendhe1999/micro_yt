package com.identity.serviceImpl;

import com.identity.entity.Authority;
import com.identity.reository.AuthorityRepository;
import com.identity.service.AuthorityService;
import com.identity.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository repository;

    @Autowired
    private JwtService jwtService;

    @Override
    public String saveAuthority(Authority authority, String token) {
        String createdByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        authority.setCreatedBy(createdByUser + " (" + role + ")");
        authority.setCreatedDate(LocalDateTime.now());
        authority.setLastModifiedBy(createdByUser + " (" + role + ")");
        authority.setLastModifiedDate(LocalDateTime.now());

        repository.save(authority);
        return "Authority '" + authority.getName() + "' created successfully by " + createdByUser;
    }

    @Override
    public List<Authority> getAllAuthorities() {
        return repository.findAll();
    }

    @Override
    public Optional<Authority> getAuthorityById(Long id) {
        return repository.findById(id);
    }

    @Override
    public String updateAuthority(Long id, Authority updatedAuthority, String token) {
        Authority existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Authority not found with id " + id));

        String modifiedByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        existing.setName(updatedAuthority.getName());
        existing.setDescription(updatedAuthority.getDescription());
        existing.setLastModifiedBy(modifiedByUser + " (" + role + ")");
        existing.setLastModifiedDate(LocalDateTime.now());

        repository.save(existing);
        return "Authority updated successfully by " + modifiedByUser;
    }

    @Override
    public String deleteAuthority(Long id, String token) {
        Authority existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Authority not found with id " + id));

        String deletedByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        repository.delete(existing);
        return "Authority deleted successfully by " + deletedByUser + " (" + role + ")";
    }

}
