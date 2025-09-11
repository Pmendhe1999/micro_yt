package com.identity.controller;

import com.identity.dto.NotificationTypeDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.NotificationType;
import com.identity.service.NotificationTypeService;
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
@RequestMapping("/notification-types")
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
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(
                    new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
        }
        String token = authHeader.replace("Bearer ", "");
        NotificationType saved = service.saveNotificationType(dto, token);
        return ResponseEntity.ok(new ResponceData("success", 200, "Created successfully", saved, 1));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllNotificationTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "notificationId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<NotificationType> result = service.getAllNotificationTypes(search, pageable);
        return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved successfully", result.getContent(), (int) result.getTotalElements()));
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getNotificationTypeById(@PathVariable Long id) {
        Optional<NotificationType> nt = service.getNotificationTypeById(id);
        if (nt.isPresent()) {
            return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved successfully", Collections.singletonList(nt.get()), 1));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponceData("fail", 404, "NotificationType not found with id " + id, Collections.emptyList(), 0));
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateNotificationType(
            @PathVariable Long id,
            @Valid @RequestBody NotificationTypeDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(
                    new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
        }
        String token = authHeader.replace("Bearer ", "");
        NotificationType updated = service.updateNotificationType(id, dto, token);
        return ResponseEntity.ok(new ResponceData("success", 200, "Updated successfully", Collections.singletonList(updated), 1));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteNotificationType(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        NotificationType deleted = service.deleteNotificationType(id, token);
        return ResponseEntity.ok(new ResponceData("success", 200, "Deleted successfully", Collections.singletonList(deleted), 1));
    }
}
