package com.identity.serviceImpl;

import com.identity.dto.ServiceProviderMasterDTO;
import com.identity.entity.ServiceProviderMaster;
import com.identity.reository.ServiceProviderMasterRepository;
import com.identity.service.JwtService;
import com.identity.service.ServiceProviderMasterService;
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
public class ServiceProviderMasterServiceImpl implements ServiceProviderMasterService {

    @Autowired
    private ServiceProviderMasterRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(ServiceProviderMasterServiceImpl.class);

    @Override
    public ServiceProviderMaster saveServiceProvider(ServiceProviderMasterDTO dto, String token) {
        try {
            if (repository.existsByName(dto.getName())) {
                log.warn("Duplicate ServiceProvider creation attempted: {}", dto.getName());
                throw new IllegalArgumentException("Service Provider already exists");
            }

            String createdBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            ServiceProviderMaster entity = new ServiceProviderMaster();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setStatus(dto.getStatus());
            entity.setCreatedDate(LocalDateTime.now());
            entity.setLastModifiedDate(LocalDateTime.now());

            ServiceProviderMaster saved = repository.save(entity);

            log.info("ServiceProvider '{}' created by user={} role={}", saved.getName(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("Error while saving ServiceProvider: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving ServiceProvider: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<ServiceProviderMaster> getAllServiceProviders(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                log.debug("Fetching ServiceProviders with filter={}", search);
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error while fetching ServiceProviders: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching ServiceProviders: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ServiceProviderMaster> getServiceProviderById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ServiceProviderMaster updateServiceProviderReturnEntity(Long id, ServiceProviderMasterDTO dto, String token) {
        try {
            ServiceProviderMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("ServiceProvider not found with id " + id));

            String modifiedBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setStatus(dto.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);

            log.info("ServiceProvider id={} updated by user={} role={}", id, modifiedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error while updating ServiceProvider id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating ServiceProvider: " + e.getMessage(), e);
        }
    }

    @Override
    public ServiceProviderMaster deleteServiceProviderReturnEntity(Long id, String token) {
        try {
            ServiceProviderMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("ServiceProvider not found with id " + id));

            String deletedBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("ServiceProvider id={} deleted by user={} role={}", id, deletedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error while deleting ServiceProvider id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting ServiceProvider: " + e.getMessage(), e);
        }
    }
    @Override
    @Transactional
    public ServiceProviderMaster patchServiceProvider(Long id, String key, Object value) {
        // ✅ Build dynamic native SQL query
        String sql = "UPDATE service_provider_master SET " + key + " = :value, last_modified_date = NOW() WHERE id = :id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("value", value);
        query.setParameter("id", id);

        int updated = query.executeUpdate();
        if (updated == 0) {
            throw new NoSuchElementException("ServiceProviderMaster not found with id " + id);
        }

        // ✅ Return updated record
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ServiceProviderMaster not found after update"));
    }
}
