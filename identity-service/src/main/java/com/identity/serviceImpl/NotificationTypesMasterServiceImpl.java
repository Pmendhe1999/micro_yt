package com.identity.serviceImpl;

import com.identity.dto.NotificationTypesMasterDTO;
import com.identity.entity.NotificationTypesMaster;
import com.identity.reository.NotificationTypesMasterRepository;
import com.identity.service.JwtService;
import com.identity.service.NotificationTypesMasterService;
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
public class NotificationTypesMasterServiceImpl implements NotificationTypesMasterService {

    @Autowired
    private NotificationTypesMasterRepository repository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EntityManager entityManager;

    private static final Logger log = LoggerFactory.getLogger(NotificationTypesMasterServiceImpl.class);

    @Override
    public NotificationTypesMaster saveNotificationType(NotificationTypesMasterDTO dto, String token) {
        try {
            if (repository.existsByName(dto.getName())) {
                log.warn("Duplicate NotificationType creation attempted: {}", dto.getName());
                throw new IllegalArgumentException("Notification Type already exists");
            }

            String createdBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            NotificationTypesMaster entity = new NotificationTypesMaster();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setStatus(dto.getStatus());
            entity.setCreatedDate(LocalDateTime.now());
            entity.setLastModifiedDate(LocalDateTime.now());

            NotificationTypesMaster saved = repository.save(entity);

            log.info("NotificationType '{}' created by user={} role={}", saved.getName(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("Error while saving NotificationType: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving NotificationType: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<NotificationTypesMaster> getAllNotificationTypes(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                log.debug("Fetching NotificationTypes with filter={}", search);
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error while fetching NotificationTypes: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching NotificationTypes: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<NotificationTypesMaster> getNotificationTypeById(Long id) {
        return repository.findById(id);
    }

    @Override
    public NotificationTypesMaster updateNotificationTypeReturnEntity(Long id, NotificationTypesMasterDTO dto, String token) {
        try {
            NotificationTypesMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("NotificationType not found with id " + id));

            String modifiedBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setStatus(dto.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);

            log.info("NotificationType id={} updated by user={} role={}", id, modifiedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error while updating NotificationType id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating NotificationType: " + e.getMessage(), e);
        }
    }

    @Override
    public NotificationTypesMaster deleteNotificationTypeReturnEntity(Long id, String token) {
        try {
            NotificationTypesMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("NotificationType not found with id " + id));

            String deletedBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("NotificationType id={} deleted by user={} role={}", id, deletedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error while deleting NotificationType id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting NotificationType: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public NotificationTypesMaster patchNotificationTypeMaster(Long id, String key, Object value) {
        // Dynamic native SQL
        String sql = "UPDATE notification_types_master SET " + key + " = :value, last_modified_date = NOW() WHERE id = :id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("value", value);
        query.setParameter("id", id);

        int updated = query.executeUpdate();
        if (updated == 0) {
            throw new NoSuchElementException("NotificationTypesMaster not found with id " + id);
        }

        // Return updated entity
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("NotificationTypesMaster not found after update"));
    }
}
