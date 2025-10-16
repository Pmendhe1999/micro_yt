package com.identity.serviceImpl;

import com.identity.controller.ApplicationController;
import com.identity.dto.CountryDTO;
import com.identity.entity.Country;
import com.identity.reository.CountryRepository;
import com.identity.service.CountryService;
import com.identity.service.JwtService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CountryServiceImpl  implements CountryService {


    @Autowired
    private CountryRepository repository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EntityManager entityManager;

    @Override
    public Country save(CountryDTO dto, String token) {
        try {
            if (repository.existsByCountryNameIgnoreCase(dto.getCountryName())) {
                log.warn("Duplicate country name: {}", dto.getCountryName());
                throw new IllegalArgumentException("Country already exists");
            }

            String createdBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            Country entity = new Country();
            entity.setCountryName(dto.getCountryName());
            entity.setCountryCode(dto.getCountryCode());
            entity.setDescription(dto.getDescription());
            entity.setStatus(dto.getStatus());
            entity.setCreatedDate(LocalDateTime.now());
            entity.setLastModifiedDate(LocalDateTime.now());

            Country saved = repository.save(entity);
            log.info("✅ Country '{}' created by {} (role={})", saved.getCountryName(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("❌ Error saving Country: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving Country: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Country> getAll(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByCountryNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching countries: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching countries: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Country> getById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching country id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching country: " + e.getMessage(), e);
        }
    }

    @Override
    public Country update(Long id, CountryDTO dto, String token) {
        try {
            Country existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Country not found with id " + id));

            String modifiedBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            existing.setCountryName(dto.getCountryName());
            existing.setCountryCode(dto.getCountryCode());
            existing.setDescription(dto.getDescription());
            existing.setStatus(dto.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);

            log.info("✏️ Country id={} updated by {} (role={})", id, modifiedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error updating Country: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating Country: " + e.getMessage(), e);
        }
    }

    @Override
    public Country delete(Long id, String token) {
        try {
            Country existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Country not found with id " + id));

            String deletedBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);
            log.info("🗑️ Country id={} deleted by {} (role={})", id, deletedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error deleting Country: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting Country: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Country patchCountry(Long id, String key, Object value) {
        String sql = "UPDATE countries SET " + key + " = :value WHERE country_id = :id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("value", value);
        query.setParameter("id", id);

        int updated = query.executeUpdate();
        if (updated == 0) {
            throw new NoSuchElementException("Country not found with id " + id);
        }

        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Country not found after patch"));
    }
}
