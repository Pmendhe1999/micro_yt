package com.identity.serviceImpl;

import com.identity.dto.NotificationTypeDTO;
import com.identity.entity.AuthTypes;
import com.identity.entity.NotificationType;
import com.identity.reository.AuthTypesRepository;
import com.identity.reository.NotificationTypeRepository;
import com.identity.service.JwtService;
import com.identity.service.NotificationTypeService;
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
public class NotificationTypeServiceImpl implements NotificationTypeService {
    private static final Logger log = LoggerFactory.getLogger(NotificationTypeServiceImpl.class);
    @Autowired
    private NotificationTypeRepository repository;

    @Autowired
    private AuthTypesRepository authTypesRepository;

    @Autowired
    private JwtService jwtService;
    @Override
    public NotificationType saveNotificationType(NotificationTypeDTO dto, String token) {
        if (repository.existsByNotificationName(dto.getNotificationName())) {
            log.warn("Duplicate NotificationType creation attempt: {}", dto.getNotificationName());
            throw new IllegalArgumentException("NotificationType already exists");
        }

        String createdByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        AuthTypes authType = authTypesRepository.findById(dto.getAuthTypeId())
                .orElseThrow(() -> new NoSuchElementException("AuthType not found"));

        NotificationType nt = new NotificationType();
        nt.setAuthType(authType);
        nt.setNotificationName(dto.getNotificationName());
        nt.setStatus(dto.getStatus());
        nt.setDescription(dto.getDescription());

        NotificationType saved = repository.save(nt);
        log.info("NotificationType '{}' created by user={} role={}", saved.getNotificationName(), createdByUser, role);
        return saved;
    }

    @Override
    public Page<NotificationType> getAllNotificationTypes(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return repository.findByNotificationNameContainingIgnoreCase(search, pageable);
        }
        return repository.findAll(pageable);
    }

    @Override
    public Optional<NotificationType> getNotificationTypeById(Long id) {
        return repository.findById(id);
    }

    @Override
    public NotificationType updateNotificationType(Long id, NotificationTypeDTO dto, String token) {
        NotificationType existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("NotificationType not found with id " + id));

        String modifiedByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        AuthTypes authType = authTypesRepository.findById(dto.getAuthTypeId())
                .orElseThrow(() -> new NoSuchElementException("AuthType not found"));

        existing.setAuthType(authType);
        existing.setNotificationName(dto.getNotificationName());
        existing.setStatus(dto.getStatus());
        existing.setDescription(dto.getDescription());

        repository.save(existing);
        log.info("NotificationType id={} updated by user={} role={}", id, modifiedByUser, role);
        return existing;
    }

    @Override
    public NotificationType deleteNotificationType(Long id, String token) {
        NotificationType existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("NotificationType not found with id " + id));

        String deletedByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        repository.delete(existing);
        log.info("NotificationType id={} deleted by user={} role={}", id, deletedByUser, role);

        return existing;
    }
}
