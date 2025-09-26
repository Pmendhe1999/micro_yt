package com.identity.serviceImpl;

import com.identity.dto.AppFunctionDTO;
import com.identity.entity.AppFunTypesMaster;
import com.identity.entity.AppFunction;
import com.identity.entity.Application;
import com.identity.reository.AppFunTypesMasterRepository;
import com.identity.reository.AppFunctionRepository;
import com.identity.reository.ApplicationRepository;
import com.identity.service.AppFunctionService;
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
public class AppFunctionServiceImpl implements AppFunctionService {

    @Autowired
    private AppFunctionRepository repository;

    @Autowired
    private AppFunTypesMasterRepository appFunTypesMasterRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(AppFunctionServiceImpl.class);

    @Override
    public AppFunction saveAppFunction(AppFunctionDTO dto, String token) {
        try {
            if (repository.existsByName(dto.getName())) {
                log.warn("Attempt to create duplicate AppFunction: {}", dto.getName());
                throw new IllegalArgumentException("App Function already exists with name: " + dto.getName());
            }

            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            AppFunTypesMaster funType = appFunTypesMasterRepository.findById(dto.getAppFunTypesMasterId())
                    .orElseThrow(() -> new NoSuchElementException("AppFunTypesMaster not found"));

            Application app = applicationRepository.findById(dto.getApplicationId())
                    .orElseThrow(() -> new NoSuchElementException("Application not found"));

            AppFunction function = new AppFunction();
            function.setName(dto.getName());
            function.setDescription(dto.getDescription());
            function.setStatus(dto.getStatus());
            function.setAppFunTypesMaster(funType);
            function.setApplication(app);
            function.setCreatedDate(LocalDateTime.now());
            function.setLastModifiedDate(LocalDateTime.now());

            AppFunction saved = repository.save(function);

            log.info("AppFunction '{}' created by user={} role={}", saved.getName(), createdByUser, role);
            return saved;

        } catch (IllegalArgumentException e) {
            log.error("Business validation failed while saving AppFunction: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while saving AppFunction: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving AppFunction: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<AppFunction> getAllAppFunctions(String search, List<Long> applicationIds, List<Long> appFunTypesMasterIds, Pageable pageable) {
        try {
            log.debug("Fetching AppFunctions with search={}, applicationIds={}, appFunTypesMasterIds={}", search, applicationIds, appFunTypesMasterIds);

            if ((search != null && !search.isEmpty()) ||
                    (applicationIds != null && !applicationIds.isEmpty()) ||
                    (appFunTypesMasterIds != null && !appFunTypesMasterIds.isEmpty())) {
                return repository.findByFilters(search, applicationIds, appFunTypesMasterIds, pageable);
            }

            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching AppFunctions: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching AppFunctions: " + e.getMessage(), e);
        }
    }


    @Override
    public Optional<AppFunction> getAppFunctionById(Long id) {
        try {
            log.debug("Fetching AppFunction by id={}", id);
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching AppFunction with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching AppFunction with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public AppFunction updateAppFunction(Long id, AppFunctionDTO dto, String token) {
        try {
            AppFunction existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted update on non-existing AppFunction with id={}", id);
                        return new NoSuchElementException("AppFunction not found with id " + id);
                    });

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            AppFunTypesMaster funType = appFunTypesMasterRepository.findById(dto.getAppFunTypesMasterId())
                    .orElseThrow(() -> new NoSuchElementException("AppFunTypesMaster not found"));

            Application app = applicationRepository.findById(dto.getApplicationId())
                    .orElseThrow(() -> new NoSuchElementException("Application not found"));

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setStatus(dto.getStatus());
            existing.setAppFunTypesMaster(funType);
            existing.setApplication(app);
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);

            log.info("AppFunction id={} updated by user={} role={}", id, modifiedByUser, role);
            return existing;

        } catch (NoSuchElementException e) {
            log.error("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating AppFunction id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating AppFunction with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public AppFunction deleteAppFunction(Long id, String token) {
        try {
            AppFunction existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted delete on non-existing AppFunction with id={}", id);
                        return new NoSuchElementException("AppFunction not found with id " + id);
                    });

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("AppFunction id={} deleted by user={} role={}", id, deletedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Delete failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while deleting AppFunction id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting AppFunction with id " + id + ": " + e.getMessage(), e);
        }
    }
}
