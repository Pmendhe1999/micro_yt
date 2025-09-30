package com.identity.serviceImpl;

import com.identity.dto.AuthTypeMasterDTO;
import com.identity.entity.AuthTypeMaster;
import com.identity.reository.AuthTypeMasterRepository;
import com.identity.service.AuthTypeMasterService;
import com.identity.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AuthTypeMasterServiceImpl implements AuthTypeMasterService {
    @Autowired
    private AuthTypeMasterRepository repository;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(AuthTypeMasterServiceImpl.class);

    @Override
    public AuthTypeMaster save(AuthTypeMasterDTO dto, String token) {
        try {
            if (repository.existsByName(dto.getName())) {
                log.warn("Duplicate AuthTypeMaster name={}", dto.getName());
                throw new IllegalArgumentException("AuthTypeMaster already exists");
            }

            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            AuthTypeMaster entity = new AuthTypeMaster();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setStatus(dto.getStatus());
            entity.setCreatedDate(LocalDateTime.now());
            entity.setLastModifiedDate(LocalDateTime.now());

            AuthTypeMaster saved = repository.save(entity);

            log.info("AuthTypeMaster '{}' created by user={} role={}", saved.getName(), createdByUser, role);

            return saved;

        } catch (Exception e) {
            log.error("Error while saving AuthTypeMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving record: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<AuthTypeMaster> getAll(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                log.debug("Fetching AuthTypeMaster with search filter={}", search);
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching AuthTypeMaster records: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching records: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<AuthTypeMaster> getById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching AuthTypeMaster by id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching record by id: " + e.getMessage(), e);
        }
    }

    @Override
    public AuthTypeMaster update(Long id, AuthTypeMasterDTO dto, String token) {
        try {
            AuthTypeMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Record not found with id " + id));

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setStatus(dto.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);

            log.info("AuthTypeMaster id={} updated by user={} role={}", id, modifiedByUser, role);

            return existing;

        } catch (Exception e) {
            log.error("Error updating AuthTypeMaster id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error updating record: " + e.getMessage(), e);
        }
    }

    @Override
    public AuthTypeMaster delete(Long id, String token) {
        try {
            AuthTypeMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Record not found with id " + id));

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("AuthTypeMaster id={} deleted by user={} role={}", id, deletedByUser, role);

            return existing;

        } catch (Exception e) {
            log.error("Error deleting AuthTypeMaster id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error deleting record: " + e.getMessage(), e);
        }
    }
}
