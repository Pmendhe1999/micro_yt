package com.identity.controller;

import com.identity.dto.AuthorityDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.Authority;
import com.identity.service.AuthorityService;
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
@RequestMapping("/authorities")
public class AuthorityController {

    @Autowired
    private AuthorityService service;

    private static final Logger log = LoggerFactory.getLogger(AuthorityController.class);

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createAuthority(
            @Valid @RequestBody AuthorityDTO authorityDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Received request to create Authority: {}", authorityDTO);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for AuthorityDTO: {}", errors);

                ResponceData response = new ResponceData("fail", 400, "Validation failed", errors, errors.size());
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            Authority savedAuthority = service.saveAuthority(authorityDTO, token);

            log.info("Authority created successfully with id={} and name={}",
                    savedAuthority.getAuthorityId(), savedAuthority.getName());

            ResponceData response = new ResponceData("success", 200, "Authority created successfully", savedAuthority, 1);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Failed to create Authority due to validation/duplicate error: {}", e.getMessage());
            ResponceData response = new ResponceData("fail", 400, e.getMessage(), null, 0);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Unexpected error while creating Authority: {}", e.getMessage(), e);
            ResponceData response = new ResponceData("error", 500, "Unexpected error: " + e.getMessage(), null, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllAuthorities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "authorityId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        try {
            log.info("Fetching all Authorities with page={}, size={}, search='{}', sortBy={}, sortDir={}",
                    page, size, search, sortBy, sortDir);

            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Authority> result = service.getAllAuthorities(search, pageable);

            log.info("Retrieved {} Authorities", result.getTotalElements());

            ResponceData response = new ResponceData("success", 200, "Retrieved Authorities", result.getContent(), (int) result.getTotalElements());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while fetching all Authorities: {}", e.getMessage(), e);
            ResponceData response = new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getAuthorityById(@PathVariable Long id) {
        try {
            log.info("Fetching Authority by id={}", id);

            Optional<Authority> authority = service.getAuthorityById(id);

            if (authority.isPresent()) {
                log.info("Found Authority with id={}", id);
                ResponceData response = new ResponceData("success", 200, "Retrieved Authority", Collections.singletonList(authority.get()), 1);
                return ResponseEntity.ok(response);
            } else {
                log.warn("Authority not found with id={}", id);
                ResponceData response = new ResponceData("fail", 404, "Authority not found with id " + id, Collections.emptyList(), 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Unexpected error while fetching Authority by id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateAuthority(
            @PathVariable Long id,
            @Valid @RequestBody AuthorityDTO updatedAuthorityDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Updating Authority with id={} and payload={}", id, updatedAuthorityDTO);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for update request on id={}, errors={}", id, errors);

                ResponceData response = new ResponceData("fail", 400, "Validation errors", Collections.emptyList(), 0);
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            Authority updatedAuthority = service.updateAuthorityReturnEntity(id, updatedAuthorityDTO, token);

            log.info("Authority updated successfully with id={}", updatedAuthority.getAuthorityId());

            ResponceData response = new ResponceData("success", 200, "Authority updated successfully", Collections.singletonList(updatedAuthority), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while updating Authority with id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteAuthority(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Deleting Authority with id={}", id);

            String token = authHeader.replace("Bearer ", "");
            Authority deletedAuthority = service.deleteAuthorityReturnEntity(id, token);

            log.info("Authority deleted successfully with id={}", deletedAuthority.getAuthorityId());

            ResponceData response = new ResponceData("success", 200, "Authority deleted successfully", Collections.singletonList(deletedAuthority), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while deleting Authority with id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
