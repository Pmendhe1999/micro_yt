package com.identity.controller;

import com.identity.dto.NotificationTypeDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.NotificationType;
import com.identity.service.NotificationTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/identity/notification-types")
@RequiredArgsConstructor
public class NotificationTypeController {
    @Autowired
    private NotificationTypeService service;

    private static final Logger log = LoggerFactory.getLogger(NotificationTypeController.class);

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createNotificationType(
            @Valid @RequestBody NotificationTypeDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
                return ResponseEntity.badRequest().body(new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            NotificationType saved = service.saveNotificationType(dto, token);

            log.info("NotificationType created successfully with id={}", saved.getId());
            return ResponseEntity.ok(new ResponceData("success", 200, "NotificationType created successfully", saved, 1));

        } catch (IllegalArgumentException e) {
            log.warn("Failed to create NotificationType: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponceData("fail", 400, e.getMessage(), null, 0));
        } catch (Exception e) {
            log.error("Unexpected error while creating NotificationType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, "Unexpected error: " + e.getMessage(), null, 0));
        }
    }

    // READ ALL (with optional filters: search, appFunctionIds, notificationTechIds, notificationTypeMasterIds)
    @GetMapping
    public ResponseEntity<ResponceData> getAllNotificationTypes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<Long> appFunctionIds,
            @RequestParam(required = false) List<Long> notificationTechIds,
            @RequestParam(required = false) List<Long> notificationTypeMasterIds,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            log.info("Fetching NotificationTypes page={}, size={}, search={}, appFunctionIds={}, notificationTechIds={}, notificationTypeMasterIds={}, sortBy={}, sortDir={}",
                    page, size, search, appFunctionIds, notificationTechIds, notificationTypeMasterIds, sortBy, sortDir);

            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            Page<NotificationType> result = service.getAllNotificationTypes(search, appFunctionIds, notificationTechIds, notificationTypeMasterIds, pageable);

            log.info("Retrieved {} NotificationTypes", result.getTotalElements());
            return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved NotificationTypes", result.getContent(), (int) result.getTotalElements()));

        } catch (Exception e) {
            log.error("Unexpected error while fetching NotificationTypes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getNotificationTypeById(@PathVariable Long id) {
        try {
            log.info("Fetching NotificationType by id={}", id);

            Optional<NotificationType> opt = service.getNotificationTypeById(id);

            if (opt.isPresent()) {
                log.info("Found NotificationType with id={}", id);
                return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved NotificationType", Collections.singletonList(opt.get()), 1));
            } else {
                log.warn("NotificationType not found with id={}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceData("fail", 404, "NotificationType not found with id " + id, Collections.emptyList(), 0));
            }
        } catch (Exception e) {
            log.error("Unexpected error while fetching NotificationType id={}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateNotificationType(
            @PathVariable Long id,
            @Valid @RequestBody NotificationTypeDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Updating NotificationType id={} with payload={}", id, dto);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for update NotificationType id={}, errors={}", id, errors);

                return ResponseEntity.badRequest().body(new ResponceData("fail", 400, "Validation errors", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            NotificationType updated = service.updateNotificationType(id, dto, token);

            log.info("NotificationType updated successfully id={}", updated.getId());
            return ResponseEntity.ok(new ResponceData("success", 200, "NotificationType updated successfully", Collections.singletonList(updated), 1));

        } catch (NoSuchElementException e) {
            log.warn("Update failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceData("fail", 404, e.getMessage(), null, 0));
        } catch (Exception e) {
            log.error("Unexpected error while updating NotificationType id={}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteNotificationType(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            NotificationType deleted = service.deleteNotificationType(id, token);

            log.info("NotificationType deleted successfully id={}", deleted.getId());
            return ResponseEntity.ok(new ResponceData("success", 200, "NotificationType deleted successfully", Collections.singletonList(deleted), 1));

        } catch (NoSuchElementException e) {
            log.warn("Delete failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceData("fail", 404, e.getMessage(), null, 0));
        } catch (Exception e) {
            log.error("Unexpected error while deleting NotificationType id={}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponceData> patchNotificationType(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No field to update");
        }

        // ✅ Expect only one key-value pair
        Map.Entry<String, Object> entry = updates.entrySet().iterator().next();
        String key = entry.getKey();
        Object value = entry.getValue();

        NotificationType updated = service.patchNotificationType(id, key, value);

        ResponceData response = new ResponceData(
                "success", 200, "NotificationType patched successfully",
                Collections.singletonList(updated), 1);

        return ResponseEntity.ok(response);
    }
}
