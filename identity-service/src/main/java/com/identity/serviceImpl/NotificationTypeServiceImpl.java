package com.identity.serviceImpl;

import com.identity.dto.NotificationTypeDTO;
import com.identity.entity.AppFunction;
import com.identity.entity.NotificationTechDetails;
import com.identity.entity.NotificationType;
import com.identity.entity.NotificationTypesMaster;
import com.identity.reository.AppFunctionRepository;
import com.identity.reository.NotificationTechDetailsRepository;
import com.identity.reository.NotificationTypeRepository;
import com.identity.reository.NotificationTypesMasterRepository;
import com.identity.service.JwtService;
import com.identity.service.NotificationTypeService;
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
public class NotificationTypeServiceImpl implements NotificationTypeService {
    @Autowired
    private NotificationTypeRepository repository;

    @Autowired
    private AppFunctionRepository appFunctionRepository;

    @Autowired
    private NotificationTechDetailsRepository notificationTechDetailsRepository;

    @Autowired
    private NotificationTypesMasterRepository notificationTypesMasterRepository;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(NotificationTypeServiceImpl.class);

    @Override
    public NotificationType saveNotificationType(NotificationTypeDTO dto, String token) {
        try {
            if (repository.existsByName(dto.getName())) {
                log.warn("Attempt to create duplicate NotificationType: {}", dto.getName());
                throw new IllegalArgumentException("NotificationType already exists with name: " + dto.getName());
            }

            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            AppFunction appFunction = appFunctionRepository.findById(dto.getAppFunctionId())
                    .orElseThrow(() -> new NoSuchElementException("AppFunction not found"));

            NotificationTechDetails tech = notificationTechDetailsRepository.findById(dto.getNotificationTechDetailsId())
                    .orElseThrow(() -> new NoSuchElementException("NotificationTechDetails not found"));

            NotificationTypesMaster master = notificationTypesMasterRepository.findById(dto.getNotificationTypesMasterId())
                    .orElseThrow(() -> new NoSuchElementException("NotificationTypesMaster not found"));

            NotificationType entity = new NotificationType();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setStatus(dto.getStatus());
            entity.setAppFunction(appFunction);
            entity.setNotificationTechDetails(tech);
            entity.setNotificationTypesMaster(master);
            entity.setCreatedDate(LocalDateTime.now());
            entity.setLastModifiedDate(LocalDateTime.now());

            NotificationType saved = repository.save(entity);

            log.info("NotificationType '{}' created by user={} role={}", saved.getName(), createdByUser, role);
            return saved;

        } catch (IllegalArgumentException e) {
            log.error("Business validation failed while saving NotificationType: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while saving NotificationType: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving NotificationType: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<NotificationType> getAllNotificationTypes(String search,
                                                          List<Long> appFunctionIds,
                                                          List<Long> notificationTechIds,
                                                          List<Long> notificationTypeMasterIds,
                                                          Pageable pageable) {
        try {
            log.debug("Fetching NotificationTypes with search={}, appFunctionIds={}, notificationTechIds={}, notificationTypeMasterIds={}",
                    search, appFunctionIds, notificationTechIds, notificationTypeMasterIds);

            if ((search != null && !search.isEmpty()) ||
                    (appFunctionIds != null && !appFunctionIds.isEmpty()) ||
                    (notificationTechIds != null && !notificationTechIds.isEmpty()) ||
                    (notificationTypeMasterIds != null && !notificationTypeMasterIds.isEmpty())) {

                return repository.findByFilters(search, appFunctionIds, notificationTechIds, notificationTypeMasterIds, pageable);
            }

            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching NotificationTypes: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching NotificationTypes: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<NotificationType> getNotificationTypeById(Long id) {
        try {
            log.debug("Fetching NotificationType by id={}", id);
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching NotificationType with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching NotificationType with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public NotificationType updateNotificationType(Long id, NotificationTypeDTO dto, String token) {
        try {
            NotificationType existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted update on non-existing NotificationType with id={}", id);
                        return new NoSuchElementException("NotificationType not found with id " + id);
                    });

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            AppFunction appFunction = appFunctionRepository.findById(dto.getAppFunctionId())
                    .orElseThrow(() -> new NoSuchElementException("AppFunction not found"));

            NotificationTechDetails tech = notificationTechDetailsRepository.findById(dto.getNotificationTechDetailsId())
                    .orElseThrow(() -> new NoSuchElementException("NotificationTechDetails not found"));

            NotificationTypesMaster master = notificationTypesMasterRepository.findById(dto.getNotificationTypesMasterId())
                    .orElseThrow(() -> new NoSuchElementException("NotificationTypesMaster not found"));

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setStatus(dto.getStatus());
            existing.setAppFunction(appFunction);
            existing.setNotificationTechDetails(tech);
            existing.setNotificationTypesMaster(master);
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);

            log.info("NotificationType id={} updated by user={} role={}", id, modifiedByUser, role);
            return existing;

        } catch (NoSuchElementException e) {
            log.error("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating NotificationType id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating NotificationType with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public NotificationType deleteNotificationType(Long id, String token) {
        try {
            NotificationType existing = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Attempted delete on non-existing NotificationType with id={}", id);
                        return new NoSuchElementException("NotificationType not found with id " + id);
                    });

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("NotificationType id={} deleted by user={} role={}", id, deletedByUser, role);
            return existing;

        } catch (NoSuchElementException e) {
            log.error("Delete failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while deleting NotificationType id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting NotificationType with id " + id + ": " + e.getMessage(), e);
        }
    }
}
