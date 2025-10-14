package com.identity.entity;

import com.identity.dto.DeviceDTO;
import com.identity.reository.DeviceRepository;
import com.identity.service.DeviceService;
import com.identity.service.JwtService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
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
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private  DeviceRepository repository;

    @Autowired
    private  JwtService jwtService;

    @Autowired
    private EntityManager entityManager;

    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Override
    public Device saveDevice(DeviceDTO dto, String token) {
        try {
            if (repository.existsByDeviceId(dto.getDeviceId())) {
                log.warn("Attempt to create duplicate Device with deviceId={}", dto.getDeviceId());
                throw new IllegalArgumentException("Device already exists");
            }

            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            Device device = new Device();
            device.setDeviceId(dto.getDeviceId());
            device.setDeviceName(dto.getDeviceName());
            device.setDeviceType(dto.getDeviceType());
            device.setIpAddress(dto.getIpAddress());
            device.setStatus(dto.getStatus());
            device.setAuthorizedAt(LocalDateTime.now());
            device.setCreatedDate(LocalDateTime.now());
            device.setLastModifiedDate(LocalDateTime.now());

            Device saved = repository.save(device);

            log.info("Device '{}' created by user={} role={}", saved.getDeviceId(), createdByUser, role);
            return saved;

        } catch (IllegalArgumentException e) {
            log.error("Business validation failed while saving Device: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while saving Device: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving device: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Device> getAllDevices(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                log.debug("Fetching Devices with search filter: {}", search);
                return repository.findByDeviceNameContainingIgnoreCase(search, pageable);
            }
            log.debug("Fetching all Devices without filter");
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching Devices: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching devices: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Device> getDeviceById(Long id) {
        try {
            log.debug("Fetching Device by id={}", id);
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching Device with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Device with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Device updateDeviceReturnEntity(Long id, DeviceDTO dto, String token) {
        try {
            Device existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted update on non-existing Device with id={}", id);
                        return new NoSuchElementException("Device not found with id " + id);
                    });

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            existing.setDeviceId(dto.getDeviceId());
            existing.setDeviceName(dto.getDeviceName());
            existing.setDeviceType(dto.getDeviceType());
            existing.setIpAddress(dto.getIpAddress());
            existing.setStatus(dto.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);

            log.info("Device id={} updated by user={} role={}", id, modifiedByUser, role);
            return existing;

        } catch (NoSuchElementException e) {
            log.error("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating Device id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating Device with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Device deleteDeviceReturnEntity(Long id, String token) {
        try {
            Device existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted delete on non-existing Device with id={}", id);
                        return new NoSuchElementException("Device not found with id " + id);
                    });

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("Device id={} deleted by user={} role={}", id, deletedByUser, role);
            return existing;

        } catch (NoSuchElementException e) {
            log.error("Delete failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while deleting Device id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting Device with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Device patchDevice(Long id, String key, Object value) {
        // Dynamic native SQL
        String sql = "UPDATE devices SET " + key + " = :value, last_modified_date = NOW() WHERE id = :id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("value", value);
        query.setParameter("id", id);

        int updated = query.executeUpdate();
        if (updated == 0) {
            throw new NoSuchElementException("Device not found with id " + id);
        }

        // Return updated entity
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Device not found after update"));
    }
}
