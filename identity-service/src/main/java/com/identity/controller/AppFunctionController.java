package com.identity.controller;

import com.identity.dto.AppFunctionDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.AppFunction;
import com.identity.service.AppFunctionService;
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

import java.util.*;

@RestController
@RequestMapping("/app-functions")
public class AppFunctionController {


    @Autowired
    private AppFunctionService service;

    private static final Logger log = LoggerFactory.getLogger(AppFunctionController.class);

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createAppFunction(
            @Valid @RequestBody AppFunctionDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Received request to create AppFunction: {}", dto);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for AppFunctionDTO: {}", errors);

                ResponceData response = new ResponceData(
                        "fail", 400, "Validation failed", errors, errors.size());
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            AppFunction saved = service.saveAppFunction(dto, token);

            log.info("AppFunction created successfully with id={}", saved.getId());

            ResponceData response = new ResponceData(
                    "success", 200, "AppFunction created successfully", saved, 1);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Failed to create AppFunction: {}", e.getMessage());
            ResponceData response = new ResponceData(
                    "fail", 400, e.getMessage(), null, 0);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Unexpected error while creating AppFunction: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "error", 500, "Unexpected error: " + e.getMessage(), null, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllAppFunctions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) List<Long> applicationIds,
            @RequestParam(required = false) List<Long> appFunTypesMasterIds) {
        try {
            log.info("Fetching AppFunctions page={}, size={}, search={}, sortBy={}, sortDir={}, appIds={}, typeIds={}",
                    page, size, search, sortBy, sortDir, applicationIds, appFunTypesMasterIds);

            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page-1, size, sort);

            Page<AppFunction> result = service.getAllAppFunctions(search, applicationIds, appFunTypesMasterIds, pageable);

            log.info("Retrieved {} AppFunctions", result.getTotalElements());

            ResponceData response = new ResponceData(
                    "success", 200, "Retrieved AppFunctions",
                    result.getContent(), (int) result.getTotalElements());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while fetching AppFunctions: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getAppFunctionById(@PathVariable Long id) {
        try {
            log.info("Fetching AppFunction by id={}", id);

            Optional<AppFunction> appFun = service.getAppFunctionById(id);

            if (appFun.isPresent()) {
                log.info("Found AppFunction with id={}", id);
                ResponceData response = new ResponceData(
                        "success", 200, "Retrieved AppFunction",
                        Collections.singletonList(appFun.get()), 1);
                return ResponseEntity.ok(response);
            } else {
                log.warn("AppFunction not found with id={}", id);
                ResponceData response = new ResponceData(
                        "fail", 404, "AppFunction not found with id " + id,
                        Collections.emptyList(), 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            log.error("Unexpected error while fetching AppFunction id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateAppFunction(
            @PathVariable Long id,
            @Valid @RequestBody AppFunctionDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Updating AppFunction id={} with payload={}", id, dto);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for update AppFunction id={}, errors={}", id, errors);

                ResponceData response = new ResponceData(
                        "fail", 400, "Validation errors", errors, errors.size());
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            AppFunction updated = service.updateAppFunction(id, dto, token);

            log.info("AppFunction updated successfully id={}", updated.getId());

            ResponceData response = new ResponceData(
                    "success", 200, "AppFunction updated successfully",
                    Collections.singletonList(updated), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while updating AppFunction id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteAppFunction(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Deleting AppFunction id={}", id);

            String token = authHeader.replace("Bearer ", "");
            AppFunction deleted = service.deleteAppFunction(id, token);

            log.info("AppFunction deleted successfully id={}", deleted.getId());

            ResponceData response = new ResponceData(
                    "success", 200, "AppFunction deleted successfully",
                    Collections.singletonList(deleted), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while deleting AppFunction id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponceData> patchAppFunction(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No field to update");
        }

        // Expecting only one key-value pair
        Map.Entry<String, Object> entry = updates.entrySet().iterator().next();
        String key = entry.getKey();
        Object value = entry.getValue();

        AppFunction updatedFunction = service.patchAppFunction(id, key, value);

        ResponceData response = new ResponceData(
                "success", 200, "App Function patched successfully",
                Collections.singletonList(updatedFunction), 1);

        return ResponseEntity.ok(response);
    }
}
