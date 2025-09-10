package com.identity.controller;

import com.identity.dto.AuthTypeDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.AuthTypes;
import com.identity.service.AuthTypesService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
public class AuthTypesController {

    @Autowired
    private AuthTypesService service;

    private static final Logger log = LoggerFactory.getLogger(AuthTypesController.class);


    @PostMapping
    public ResponseEntity<ResponceData> createAuthType(
            @Valid @RequestBody AuthTypeDTO authTypeDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Received request to create AuthType: {}", authTypeDTO);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

                log.warn("Validation failed for AuthTypeDTO: {}", errors);

                ResponceData response = new ResponceData(
                        "fail",
                        400,
                        "Validation failed",
                        errors,
                        errors.size()
                );
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            log.debug("Extracted token for createAuthType request");

            AuthTypes savedAuthType = service.saveAuthType(authTypeDTO, token);
            log.info("AuthType created successfully with id={} and name={}",
                    savedAuthType.getAuthTypeId(),
                    savedAuthType.getAuthTypeName());

            ResponceData response = new ResponceData(
                    "success",
                    200,
                    "Auth Type created successfully",
                    savedAuthType,
                    1
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Failed to create AuthType due to validation/duplicate error: {}", e.getMessage());

            ResponceData response = new ResponceData(
                    "fail",
                    400,
                    e.getMessage(),
                    null,
                    0
            );
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("Unexpected error while creating AuthType: {}", e.getMessage(), e);

            ResponceData response = new ResponceData(
                    "error",
                    500,
                    "Unexpected error: " + e.getMessage(),
                    null,
                    0
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ ALL
    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllAuthTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "authTypeId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        try {
            log.info("Fetching all AuthTypes with page={}, size={}, search='{}', sortBy={}, sortDir={}",
                    page, size, search, sortBy, sortDir);

            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<AuthTypes> result = service.getAllAuthTypes(search, pageable);

            log.info("Retrieved {} AuthTypes", result.getTotalElements());

            ResponceData response = new ResponceData(
                    "success",
                    200,
                    "Retrieved Auth Types",
                    result.getContent(),
                    (int) result.getTotalElements()
            );

            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            log.warn("No AuthTypes found: {}", e.getMessage());
            ResponceData response = new ResponceData(
                    "fail",
                    404,
                    e.getMessage(),
                    Collections.emptyList(),
                    0
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            log.error("Unexpected error while fetching all AuthTypes: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail",
                    500,
                    "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(),
                    0
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getAuthTypeById(@PathVariable Long id) {
        try {
            log.info("Fetching AuthType by id={}", id);

            Optional<AuthTypes> authType = service.getAuthTypeById(id);

            if (authType.isPresent()) {
                log.info("Found AuthType with id={}", id);
                ResponceData response = new ResponceData(
                        "success",
                        200,
                        "Retrieved Auth Type",
                        Collections.singletonList(authType.get()),
                        1
                );
                return ResponseEntity.ok(response);
            } else {
                log.warn("AuthType not found with id={}", id);
                ResponceData response = new ResponceData(
                        "fail",
                        404,
                        "Auth type not found with id " + id,
                        Collections.emptyList(),
                        0
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (NoSuchElementException e) {
            log.warn("Error fetching AuthType by id={}, reason={}", id, e.getMessage());
            ResponceData response = new ResponceData(
                    "fail",
                    404,
                    e.getMessage(),
                    Collections.emptyList(),
                    0
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            log.error("Unexpected error while fetching AuthType by id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail",
                    500,
                    "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(),
                    0
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateAuthType(
            @PathVariable Long id,
            @Valid @RequestBody AuthTypeDTO updatedAuthTypeDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Updating AuthType with id={} and payload={}", id, updatedAuthTypeDTO);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for update request on id={}, errors={}", id, errors);

                ResponceData response = new ResponceData(
                        "fail",
                        400,
                        "Validation errors",
                        Collections.emptyList(),
                        0
                );
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            AuthTypes updatedAuthType = service.updateAuthTypeReturnEntity(id, updatedAuthTypeDTO, token);

            log.info("AuthType updated successfully with id={}", updatedAuthType.getAuthTypeId());

            ResponceData response = new ResponceData(
                    "success",
                    200,
                    "Auth type updated successfully",
                    Collections.singletonList(updatedAuthType),
                    1
            );

            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            log.warn("AuthType not found while updating id={}, reason={}", id, e.getMessage());
            ResponceData response = new ResponceData(
                    "fail",
                    404,
                    e.getMessage(),
                    Collections.emptyList(),
                    0
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            log.error("Unexpected error while updating AuthType with id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail",
                    500,
                    "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(),
                    0
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteAuthType(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Deleting AuthType with id={}", id);

            String token = authHeader.replace("Bearer ", "");
            AuthTypes deletedAuthType = service.deleteAuthTypeReturnEntity(id, token);

            log.info("AuthType deleted successfully with id={}", deletedAuthType.getAuthTypeId());

            ResponceData response = new ResponceData(
                    "success",
                    200,
                    "Auth type deleted successfully",
                    Collections.singletonList(deletedAuthType),
                    1
            );

            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            log.warn("AuthType not found while deleting id={}, reason={}", id, e.getMessage());
            ResponceData response = new ResponceData(
                    "fail",
                    404,
                    e.getMessage(),
                    Collections.emptyList(),
                    0
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            log.error("Unexpected error while deleting AuthType with id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail",
                    500,
                    "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(),
                    0
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
