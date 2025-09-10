package com.identity.serviceImpl;

import com.identity.dto.AuthTypeDTO;
import com.identity.entity.AuthTypes;
import com.identity.reository.AuthTypesRepository;
import com.identity.service.AuthTypesService;
import com.identity.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@RequiredArgsConstructor
public class AuthTypesServiceImpl implements AuthTypesService {

    @Autowired
    private AuthTypesRepository repository;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(AuthTypesServiceImpl.class);


    @Override
    public AuthTypes saveAuthType(AuthTypeDTO authTypeDTO, String token) {
        try {
            if (repository.existsByAuthTypeName(authTypeDTO.getAuthTypeName())) {
                log.warn("Attempt to create duplicate AuthType: {}", authTypeDTO.getAuthTypeName());
                throw new IllegalArgumentException("Auth type already exists");
            }

            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            AuthTypes authType = new AuthTypes();
            authType.setAuthTypeName(authTypeDTO.getAuthTypeName());
            authType.setStatus(authTypeDTO.getStatus());
            authType.setFunctionality(authTypeDTO.getFunctionality());
            authType.setDescription(authTypeDTO.getDescription());

            AuthTypes savedAuthType = repository.save(authType);

            log.info("AuthType '{}' created by user={} role={}",
                    savedAuthType.getAuthTypeName(), createdByUser, role);

            return savedAuthType;

        } catch (IllegalArgumentException e) {
            log.error("Business validation failed while saving AuthType: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while saving AuthType: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving auth type: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<AuthTypes> getAllAuthTypes(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                log.debug("Fetching AuthTypes with search filter: {}", search);
                return repository.findByAuthTypeNameContainingIgnoreCase(search, pageable);
            }
            log.debug("Fetching all AuthTypes without filter");
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching AuthTypes: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Auth Types: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<AuthTypes> getAuthTypeById(Long id) {
        try {
            log.debug("Fetching AuthType by id={}", id);
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching AuthType with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Auth Type with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public AuthTypes updateAuthTypeReturnEntity(Long id, AuthTypeDTO updatedAuthTypeDTO, String token) {
        try {
            AuthTypes existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted update on non-existing AuthType with id={}", id);
                        return new NoSuchElementException("Auth type not found with id " + id);
                    });

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            existing.setAuthTypeName(updatedAuthTypeDTO.getAuthTypeName());
            existing.setStatus(updatedAuthTypeDTO.getStatus());
            existing.setFunctionality(updatedAuthTypeDTO.getFunctionality());
            existing.setDescription(updatedAuthTypeDTO.getDescription());

            repository.save(existing);

            log.info("AuthType id={} updated by user={} role={}", id, modifiedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating AuthType id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating Auth Type with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public AuthTypes deleteAuthTypeReturnEntity(Long id, String token) {
        try {
            AuthTypes existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted delete on non-existing AuthType with id={}", id);
                        return new NoSuchElementException("Auth type not found with id " + id);
                    });

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("AuthType id={} deleted by user={} role={}", id, deletedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Delete failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while deleting AuthType id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting Auth Type with id " + id + ": " + e.getMessage(), e);
        }
    }
}
