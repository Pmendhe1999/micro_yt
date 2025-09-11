package com.identity.serviceImpl;

import com.identity.dto.ApplicationDTO;
import com.identity.entity.Application;
import com.identity.entity.AuthTypes;
import com.identity.reository.ApplicationRepository;
import com.identity.reository.AuthTypesRepository;
import com.identity.service.ApplicationService;
import com.identity.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl  implements ApplicationService {

    @Autowired
    private ApplicationRepository repository;

    @Autowired
    private AuthTypesRepository authTypesRepository;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Override
    public Application saveApplication(ApplicationDTO applicationDTO, String token) {
        try {
            if (repository.existsByApplicationName(applicationDTO.getApplicationName())) {
                log.warn("Attempt to create duplicate Application: {}", applicationDTO.getApplicationName());
                throw new IllegalArgumentException("Application already exists");
            }

            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            AuthTypes authType = authTypesRepository.findById(applicationDTO.getAuthTypeId())
                    .orElseThrow(() -> new NoSuchElementException("AuthType not found"));

            Application app = new Application();
            app.setApplicationName(applicationDTO.getApplicationName());
            app.setAuthType(authType);
            app.setDescription(applicationDTO.getDescription());

            Application savedApp = repository.save(app);

            log.info("Application '{}' created by user={} role={}",
                    savedApp.getApplicationName(), createdByUser, role);

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
                return repository.findByApplicationNameContainingIgnoreCase(search, pageable);
            }
            log.debug("Fetching all Applications without filter");
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching Applications: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Applications: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Application> getApplicationById(Long id) {
        try {
            log.debug("Fetching Application by id={}", id);
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching Application with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Application with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Application updateApplicationReturnEntity(Long id, ApplicationDTO updatedApplicationDTO, String token) {
        try {
            Application existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted update on non-existing Application with id={}", id);
                        return new NoSuchElementException("Application not found with id " + id);
                    });

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            AuthTypes authType = authTypesRepository.findById(updatedApplicationDTO.getAuthTypeId())
                    .orElseThrow(() -> new NoSuchElementException("AuthType not found"));

            existing.setApplicationName(updatedApplicationDTO.getApplicationName());
            existing.setAuthType(authType);
            existing.setDescription(updatedApplicationDTO.getDescription());

            repository.save(existing);

            log.info("Application id={} updated by user={} role={}", id, modifiedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating Application id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating Application with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Application deleteApplicationReturnEntity(Long id, String token) {
        try {
            Application existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted delete on non-existing Application with id={}", id);
                        return new NoSuchElementException("Application not found with id " + id);
                    });

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("Application id={} deleted by user={} role={}", id, deletedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Delete failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while deleting Application id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting Application with id " + id + ": " + e.getMessage(), e);
        }
    }

}
