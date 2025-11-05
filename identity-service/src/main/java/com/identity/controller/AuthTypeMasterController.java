package com.identity.controller;

import com.identity.dto.AuthTypeMasterDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.AuthTypeMaster;
import com.identity.service.AuthTypeMasterService;
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
@RequestMapping("/identity/auth-types-master")
public class AuthTypeMasterController {
    @Autowired
    private AuthTypeMasterService service;

    private static final Logger log = LoggerFactory.getLogger(AuthTypeMasterController.class);

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> create(
            @Valid @RequestBody AuthTypeMasterDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(
                        new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            AuthTypeMaster saved = service.save(dto, token);

            return ResponseEntity.ok(new ResponceData("success", 200, "Created successfully", saved, 1));

        } catch (Exception e) {
            log.error("Error creating AuthTypeMaster: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, "Unexpected error: " + e.getMessage(), null, 0));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            Page<AuthTypeMaster> result = service.getAll(search, pageable);

            return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved records",
                    result.getContent(), (int) result.getTotalElements()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(),
                            Collections.emptyList(), 0));
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getById(@PathVariable Long id) {
        try {
            Optional<AuthTypeMaster> entity = service.getById(id);
            if (entity.isPresent()) {
                return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved record",
                        Collections.singletonList(entity.get()), 1));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponceData("fail", 404, "Record not found with id " + id,
                                Collections.emptyList(), 0));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(),
                            Collections.emptyList(), 0));
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> update(
            @PathVariable Long id,
            @Valid @RequestBody AuthTypeMasterDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(new ResponceData("fail", 400, "Validation errors", Collections.emptyList(), 0));
            }

            String token = authHeader.replace("Bearer ", "");
            AuthTypeMaster updated = service.update(id, dto, token);

            return ResponseEntity.ok(new ResponceData("success", 200, "Updated successfully",
                    Collections.singletonList(updated), 1));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(),
                            Collections.emptyList(), 0));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            AuthTypeMaster deleted = service.delete(id, token);

            return ResponseEntity.ok(new ResponceData("success", 200, "Deleted successfully",
                    Collections.singletonList(deleted), 1));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(),
                            Collections.emptyList(), 0));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponceData> patchAuthTypeMaster(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No field to update");
        }

        // ✅ Expect only one key-value pair
        Map.Entry<String, Object> entry = updates.entrySet().iterator().next();
        String key = entry.getKey();
        Object value = entry.getValue();

        AuthTypeMaster updated = service.patchAuthTypeMaster(id, key, value);

        ResponceData response = new ResponceData(
                "success", 200, "AuthTypeMaster patched successfully",
                Collections.singletonList(updated), 1);

        return ResponseEntity.ok(response);
    }
}
