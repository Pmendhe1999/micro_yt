package com.identity.controller;

import com.identity.dto.AuthTypeDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.AuthType;
import com.identity.service.AuthTypeService;
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
@RequestMapping("/auth-types")
@RequiredArgsConstructor
public class AuthTypeController {

    @Autowired
    private  AuthTypeService service;
    private static final Logger log = LoggerFactory.getLogger(AuthTypeController.class);
    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createAuthType(
            @Valid @RequestBody AuthTypeDTO dto,
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
            AuthType saved = service.saveAuthType(dto, token);

            return ResponseEntity.ok(new ResponceData("success", 200, "AuthType created successfully", saved, 1));
        } catch (Exception e) {
            log.error("Error while creating AuthType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllAuthTypes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) List<Long> authFuncIds,
            @RequestParam(required = false) List<Long> authTypeMasterIds) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<AuthType> result = service.getAllAuthTypes(search, authFuncIds, authTypeMasterIds, pageable);
        return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved AuthTypes",
                result.getContent(), (int) result.getTotalElements()));
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getAuthTypeById(@PathVariable Long id) {
        Optional<AuthType> authType = service.getAuthTypeById(id);
        return authType.map(value ->
                        ResponseEntity.ok(new ResponceData("success", 200, "Retrieved AuthType",
                                Collections.singletonList(value), 1)))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ResponceData("fail", 404, "AuthType not found", null, 0)));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateAuthType(
            @PathVariable Long id,
            @Valid @RequestBody AuthTypeDTO dto,
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
            AuthType updated = service.updateAuthType(id, dto, token);

            return ResponseEntity.ok(new ResponceData("success", 200, "AuthType updated successfully",
                    Collections.singletonList(updated), 1));
        } catch (Exception e) {
            log.error("Error while updating AuthType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteAuthType(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            AuthType deleted = service.deleteAuthType(id, token);

            return ResponseEntity.ok(new ResponceData("success", 200, "AuthType deleted successfully",
                    Collections.singletonList(deleted), 1));
        } catch (Exception e) {
            log.error("Error while deleting AuthType: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }
}
