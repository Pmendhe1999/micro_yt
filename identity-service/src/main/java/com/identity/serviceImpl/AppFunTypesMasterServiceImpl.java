package com.identity.serviceImpl;

import com.identity.dto.AppFunTypesMasterDTO;
import com.identity.entity.AppFunTypesMaster;
import com.identity.reository.AppFunTypesMasterRepository;
import com.identity.service.AppFunTypesMasterService;
import com.identity.service.JwtService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
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
public class AppFunTypesMasterServiceImpl implements AppFunTypesMasterService {

    @Autowired
    private AppFunTypesMasterRepository repository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EntityManager entityManager;

    private static final Logger log = LoggerFactory.getLogger(AppFunTypesMasterServiceImpl.class);

    @Override
    public AppFunTypesMaster save(AppFunTypesMasterDTO dto, String token) {
        try {
            if (repository.existsByName(dto.getName())) {
                log.warn("Attempt to create duplicate AppFunTypesMaster: {}", dto.getName());
                throw new IllegalArgumentException("AppFunTypesMaster already exists");
            }

            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            AppFunTypesMaster entity = new AppFunTypesMaster();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setStatus(dto.getStatus());
            entity.setCreatedDate(LocalDateTime.now());
            entity.setLastModifiedDate(LocalDateTime.now());

            AppFunTypesMaster saved = repository.save(entity);

            log.info("AppFunTypesMaster '{}' created by user={} role={}",
                    saved.getName(), createdByUser, role);

            return saved;

        } catch (IllegalArgumentException e) {
            log.error("Business validation failed while saving AppFunTypesMaster: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while saving AppFunTypesMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving record: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<AppFunTypesMaster> getAll(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                log.debug("Fetching AppFunTypesMaster with search filter: {}", search);
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            log.debug("Fetching all AppFunTypesMaster records");
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching AppFunTypesMaster records: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching records: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<AppFunTypesMaster> getById(Long id) {
        try {
            log.debug("Fetching AppFunTypesMaster by id={}", id);
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching AppFunTypesMaster with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching record with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public AppFunTypesMaster update(Long id, AppFunTypesMasterDTO dto, String token) {
        try {
            AppFunTypesMaster existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted update on non-existing AppFunTypesMaster with id={}", id);
                        return new NoSuchElementException("Record not found with id " + id);
                    });

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setStatus(dto.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);

            log.info("AppFunTypesMaster id={} updated by user={} role={}", id, modifiedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating AppFunTypesMaster id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating record with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public AppFunTypesMaster delete(Long id, String token) {
        try {
            AppFunTypesMaster existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted delete on non-existing AppFunTypesMaster with id={}", id);
                        return new NoSuchElementException("Record not found with id " + id);
                    });

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("AppFunTypesMaster id={} deleted by user={} role={}", id, deletedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Delete failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while deleting AppFunTypesMaster id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting record with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public AppFunTypesMaster patchAppFunType(Long id, String key, Object value) {
        // Build dynamic SQL query
        String sql = "UPDATE app_fun_types_master SET " + key + " = :value, last_modified_date = NOW() WHERE id = :id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("value", value);
        query.setParameter("id", id);

        int updated = query.executeUpdate();
        if (updated == 0) {
            throw new NoSuchElementException("AppFunTypesMaster not found with id " + id);
        }

        // Return updated record
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("AppFunTypesMaster not found after update"));
    }
}
