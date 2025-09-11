package com.identity.serviceImpl;

import com.identity.dto.AuthorityDTO;
import com.identity.entity.Authority;
import com.identity.reository.AuthorityRepository;
import com.identity.service.AuthorityService;
import com.identity.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private static final Logger log = LoggerFactory.getLogger(AuthorityServiceImpl.class);


    @Override
    public Authority saveAuthority(AuthorityDTO authorityDTO, String token) {
        try {
            if (repository.existsByName(authorityDTO.getName())) {
                log.warn("Attempt to create duplicate Authority: {}", authorityDTO.getName());
                throw new IllegalArgumentException("Authority already exists");
            }

            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            Authority authority = new Authority();
            authority.setName(authorityDTO.getName());

            Authority savedAuthority = repository.save(authority);

            log.info("Authority '{}' created by user={} role={}",
                    savedAuthority.getName(), createdByUser, role);

            return savedAuthority;

        } catch (IllegalArgumentException e) {
            log.error("Business validation failed while saving Authority: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while saving Authority: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving authority: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Authority> getAllAuthorities(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                log.debug("Fetching Authorities with search filter: {}", search);
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            log.debug("Fetching all Authorities without filter");
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching Authorities: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Authorities: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Authority> getAuthorityById(Long id) {
        try {
            log.debug("Fetching Authority by id={}", id);
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching Authority with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Authority with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Authority updateAuthorityReturnEntity(Long id, AuthorityDTO updatedAuthorityDTO, String token) {
        try {
            Authority existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted update on non-existing Authority with id={}", id);
                        return new NoSuchElementException("Authority not found with id " + id);
                    });

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            existing.setName(updatedAuthorityDTO.getName());

            repository.save(existing);

            log.info("Authority id={} updated by user={} role={}", id, modifiedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating Authority id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating Authority with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Authority deleteAuthorityReturnEntity(Long id, String token) {
        try {
            Authority existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted delete on non-existing Authority with id={}", id);
                        return new NoSuchElementException("Authority not found with id " + id);
                    });

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("Authority id={} deleted by user={} role={}", id, deletedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Delete failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while deleting Authority id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting Authority with id " + id + ": " + e.getMessage(), e);
        }
    }

}
