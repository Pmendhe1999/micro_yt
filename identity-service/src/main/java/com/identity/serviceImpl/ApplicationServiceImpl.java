package com.identity.serviceImpl;

import com.identity.dto.ApplicationDTO;
import com.identity.entity.Application;
import com.identity.reository.ApplicationRepository;
import com.identity.service.ApplicationService;
import com.identity.service.JwtService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ApplicationServiceImpl  implements ApplicationService {

    @Autowired
    private ApplicationRepository repository;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Override
    public Application saveApplication(ApplicationDTO applicationDTO, String token) {
        try {
            if (repository.existsByName(applicationDTO.getName())) {
                log.warn("Attempt to create duplicate Application: {}", applicationDTO.getName());
                throw new IllegalArgumentException("Application already exists");
            }

            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            Application app = new Application();
            app.setName(applicationDTO.getName());
            app.setDescription(applicationDTO.getDescription());
            app.setStatus(applicationDTO.getStatus());
            app.setCreatedDate(LocalDateTime.now());
            app.setLastModifiedDate(LocalDateTime.now());

            Application savedApp = repository.save(app);

            log.info("Application '{}' created by user={} role={}",
                    savedApp.getName(), createdByUser, role);

            return savedApp;

        } catch (IllegalArgumentException e) {
            log.error("Business validation failed while saving Application: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while saving Application: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving application: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Application> getAllApplications(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                log.debug("Fetching Applications with search filter: {}", search);
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            log.debug("Fetching all Applications without filter");
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching Applications: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Applications: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Application> getApplicationById(Long applicationId) {
        try {
            log.debug("Fetching Application by applicationId={}", applicationId);
            return repository.findById(applicationId);
        } catch (Exception e) {
            log.error("Error occurred while fetching Application with applicationId={}: {}", applicationId, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Application with applicationId " + applicationId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Application updateApplicationReturnEntity(Long applicationId, ApplicationDTO updatedApplicationDTO, String token) {
        try {
            Application existing = repository.findById(applicationId)
                    .orElseThrow(() -> {
                        log.warn("Attempted update on non-existing Application with applicationId={}", applicationId);
                        return new NoSuchElementException("Application not found with id " + applicationId);
                    });

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            existing.setName(updatedApplicationDTO.getName());
            existing.setDescription(updatedApplicationDTO.getDescription());
            existing.setStatus(updatedApplicationDTO.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);

            log.info("Application applicationId={} updated by user={} role={}", applicationId, modifiedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating Application applicationId={}: {}", applicationId, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating Application with id " + applicationId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Application deleteApplicationReturnEntity(Long applicationId, String token) {
        try {
            Application existing = repository.findById(applicationId)
                    .orElseThrow(() -> {
                        log.warn("Attempted delete on non-existing Application with applicationId={}", applicationId);
                        return new NoSuchElementException("Application not found with id " + applicationId);
                    });

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("Application applicationId={} deleted by user={} role={}", applicationId, deletedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Delete failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while deleting Application applicationId={}: {}", applicationId, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting Application with id " + applicationId + ": " + e.getMessage(), e);
        }
    }
}
