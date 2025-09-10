package com.identity.controller;

import com.identity.entity.Authority;
import com.identity.service.AuthorityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/authorities")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    // CREATE
    @PostMapping
    public ResponseEntity<?> createAuthority(@Valid @RequestBody Authority authority,
                                             BindingResult bindingResult,
                                             @RequestHeader("Authorization") String authHeader) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(errors);
            }

            String token = authHeader.replace("Bearer ", "");
            String response = authorityService.saveAuthority(authority, token);

            return ResponseEntity.ok(Collections.singletonMap("message", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Unexpected error: " + e.getMessage()));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Authority>> getAllAuthorities() {
        return ResponseEntity.ok(authorityService.getAllAuthorities());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorityById(@PathVariable Long id) {
        return authorityService.getAuthorityById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Authority not found")));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthority(@PathVariable Long id,
                                             @Valid @RequestBody Authority updatedAuthority,
                                             BindingResult bindingResult,
                                             @RequestHeader("Authorization") String authHeader) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(errors);
            }

            String token = authHeader.replace("Bearer ", "");
            String response = authorityService.updateAuthority(id, updatedAuthority, token);

            return ResponseEntity.ok(Collections.singletonMap("message", response));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Unexpected error: " + e.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthority(@PathVariable Long id,
                                             @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String response = authorityService.deleteAuthority(id, token);

            return ResponseEntity.ok(Collections.singletonMap("message", response));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Unexpected error: " + e.getMessage()));
        }
    }
}
