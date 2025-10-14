package com.identity.controller;

import com.identity.dto.NotificationTypesMasterDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.NotificationTypesMaster;
import com.identity.service.NotificationTypesMasterService;
import jakarta.validation.Valid;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/notification-types-master")
public class NotificationTypesMasterController {

    private static final Logger log = LoggerFactory.getLogger(NotificationTypesMasterController.class);

    @Autowired
    private NotificationTypesMasterService service;
    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createNotificationType(
            @Valid @RequestBody NotificationTypesMasterDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
                return ResponseEntity.badRequest().body(new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            NotificationTypesMaster saved = service.saveNotificationType(dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "NotificationType created", saved, 1));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllNotificationTypes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            Page<NotificationTypesMaster> result = service.getAllNotificationTypes(search, pageable);
            return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved NotificationTypes", result.getContent(), (int) result.getTotalElements()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getNotificationTypeById(@PathVariable Long id) {
        Optional<NotificationTypesMaster> entity = service.getNotificationTypeById(id);
        if (entity.isPresent()) {
            return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved NotificationType", Collections.singletonList(entity.get()), 1));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponceData("fail", 404, "NotificationType not found", Collections.emptyList(), 0));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateNotificationType(
            @PathVariable Long id,
            @Valid @RequestBody NotificationTypesMasterDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
                return ResponseEntity.badRequest().body(new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            NotificationTypesMaster updated = service.updateNotificationTypeReturnEntity(id, dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "NotificationType updated", Collections.singletonList(updated), 1));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteNotificationType(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            NotificationTypesMaster deleted = service.deleteNotificationTypeReturnEntity(id, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "NotificationType deleted", Collections.singletonList(deleted), 1));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponceData> patchNotificationTypeMaster(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No field to update");
        }

        // Only one key-value expected
        Map.Entry<String, Object> entry = updates.entrySet().iterator().next();
        String key = entry.getKey();
        Object value = entry.getValue();

        NotificationTypesMaster updated = service.patchNotificationTypeMaster(id, key, value);

        ResponceData response = new ResponceData(
                "success", 200, "NotificationTypesMaster patched successfully",
                Collections.singletonList(updated), 1);

        return ResponseEntity.ok(response);
    }
}
