package com.identity.controller;

import com.identity.dto.ApplicationDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.Application;
import com.identity.service.ApplicationService;
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
@RequestMapping("/identity/applications")
public class ApplicationController {


    @Autowired
    private ApplicationService service;

    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createApplication(
            @Valid @RequestBody ApplicationDTO applicationDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Received request to create Application: {}", applicationDTO);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

                log.warn("Validation failed for ApplicationDTO: {}", errors);

                ResponceData response = new ResponceData(
                        "fail", 400, "Validation failed", errors, errors.size());
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            Application savedApp = service.saveApplication(applicationDTO, token);

            log.info("Application created successfully with id={} and name={}",
                    savedApp.getApplicationId(), savedApp.getName());

            ResponceData response = new ResponceData(
                    "success", 200, "Application created successfully", savedApp, 1);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Failed to create Application: {}", e.getMessage());
            ResponceData response = new ResponceData(
                    "fail", 400, e.getMessage(), null, 0);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Unexpected error while creating Application: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "error", 500, "Unexpected error: " + e.getMessage(), null, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllApplications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "applicationId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            log.info("Fetching Applications page={}, size={}, search={}, sortBy={}, sortDir={}",
                    page, size, search, sortBy, sortDir);

            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page-1, size, sort);

            Page<Application> result = service.getAllApplications(search, pageable);

            log.info("Retrieved {} Applications", result.getTotalElements());

            ResponceData response = new ResponceData(
                    "success", 200, "Retrieved Applications",
                    result.getContent(), (int) result.getTotalElements());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while fetching Applications: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getApplicationById(@PathVariable Long id) {
        try {
            log.info("Fetching Application by id={}", id);

            Optional<Application> app = service.getApplicationById(id);

            if (app.isPresent()) {
                log.info("Found Application with id={}", id);
                ResponceData response = new ResponceData(
                        "success", 200, "Retrieved Application",
                        Collections.singletonList(app.get()), 1);
                return ResponseEntity.ok(response);
            } else {
                log.warn("Application not found with id={}", id);
                ResponceData response = new ResponceData(
                        "fail", 404, "Application not found with id " + id,
                        Collections.emptyList(), 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            log.error("Unexpected error while fetching Application id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationDTO updatedApplicationDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Updating Application id={} with payload={}", id, updatedApplicationDTO);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for update Application id={}, errors={}", id, errors);

                ResponceData response = new ResponceData(
                        "fail", 400, "Validation errors",
                        Collections.emptyList(), 0);
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            Application updatedApp = service.updateApplicationReturnEntity(id, updatedApplicationDTO, token);

            log.info("Application updated successfully id={}", updatedApp.getApplicationId());

            ResponceData response = new ResponceData(
                    "success", 200, "Application updated successfully",
                    Collections.singletonList(updatedApp), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while updating Application id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteApplication(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Deleting Application id={}", id);

            String token = authHeader.replace("Bearer ", "");
            Application deletedApp = service.deleteApplicationReturnEntity(id, token);

            log.info("Application deleted successfully id={}", deletedApp.getApplicationId());

            ResponceData response = new ResponceData(
                    "success", 200, "Application deleted successfully",
                    Collections.singletonList(deletedApp), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while deleting Application id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponceData> patchApplication(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No field to update");
        }

        // Expect only one key-value pair
        Map.Entry<String, Object> entry = updates.entrySet().iterator().next();
        String key = entry.getKey();
        Object value = entry.getValue();

        Application updatedApp = service.patchApplication(id, key, value);

        ResponceData response = new ResponceData(
                "success", 200, "Application patched successfully",
                Collections.singletonList(updatedApp), 1);

        return ResponseEntity.ok(response);
    }
}
