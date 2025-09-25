package com.identity.controller;

import com.identity.dto.AppFunTypesMasterDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.AppFunTypesMaster;
import com.identity.service.AppFunTypesMasterService;
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
@RequestMapping("/app-fun-types")
public class AppFunTypesMasterController {

    @Autowired
    private AppFunTypesMasterService service;

    private static final Logger log = LoggerFactory.getLogger(AppFunTypesMasterController.class);

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> create(
            @Valid @RequestBody AppFunTypesMasterDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Received request to create AppFunTypesMaster: {}", dto);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for AppFunTypesMasterDTO: {}", errors);

                ResponceData response = new ResponceData(
                        "fail", 400, "Validation failed", errors, errors.size());
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            AppFunTypesMaster saved = service.save(dto, token);

            log.info("AppFunTypesMaster created successfully with id={} and name={}",
                    saved.getId(), saved.getName());

            ResponceData response = new ResponceData(
                    "success", 200, "Created successfully", saved, 1);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Failed to create AppFunTypesMaster: {}", e.getMessage());
            ResponceData response = new ResponceData(
                    "fail", 400, e.getMessage(), null, 0);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Unexpected error while creating AppFunTypesMaster: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "error", 500, "Unexpected error: " + e.getMessage(), null, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            log.info("Fetching AppFunTypesMaster page={}, size={}, search={}, sortBy={}, sortDir={}",
                    page, size, search, sortBy, sortDir);

            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<AppFunTypesMaster> result = service.getAll(search, pageable);

            log.info("Retrieved {} AppFunTypesMaster records", result.getTotalElements());

            ResponceData response = new ResponceData(
                    "success", 200, "Retrieved records",
                    result.getContent(), (int) result.getTotalElements());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while fetching AppFunTypesMaster: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getById(@PathVariable Long id) {
        try {
            log.info("Fetching AppFunTypesMaster by id={}", id);

            Optional<AppFunTypesMaster> entity = service.getById(id);

            if (entity.isPresent()) {
                log.info("Found AppFunTypesMaster with id={}", id);
                ResponceData response = new ResponceData(
                        "success", 200, "Retrieved record",
                        Collections.singletonList(entity.get()), 1);
                return ResponseEntity.ok(response);
            } else {
                log.warn("AppFunTypesMaster not found with id={}", id);
                ResponceData response = new ResponceData(
                        "fail", 404, "Record not found with id " + id,
                        Collections.emptyList(), 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            log.error("Unexpected error while fetching AppFunTypesMaster id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> update(
            @PathVariable Long id,
            @Valid @RequestBody AppFunTypesMasterDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Updating AppFunTypesMaster id={} with payload={}", id, dto);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for update id={}, errors={}", id, errors);

                ResponceData response = new ResponceData(
                        "fail", 400, "Validation errors", Collections.emptyList(), 0);
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            AppFunTypesMaster updated = service.update(id, dto, token);

            log.info("AppFunTypesMaster updated successfully id={}", updated.getId());

            ResponceData response = new ResponceData(
                    "success", 200, "Updated successfully",
                    Collections.singletonList(updated), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while updating AppFunTypesMaster id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Deleting AppFunTypesMaster id={}", id);

            String token = authHeader.replace("Bearer ", "");
            AppFunTypesMaster deleted = service.delete(id, token);

            log.info("AppFunTypesMaster deleted successfully id={}", deleted.getId());

            ResponceData response = new ResponceData(
                    "success", 200, "Deleted successfully",
                    Collections.singletonList(deleted), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while deleting AppFunTypesMaster id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
