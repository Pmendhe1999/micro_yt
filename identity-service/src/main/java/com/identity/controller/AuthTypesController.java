package com.identity.controller;

import com.identity.entity.AuthTypes;
import com.identity.service.AuthTypesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseEntity<?> createAuthType(@Valid @RequestBody AuthTypes authType,
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
            String response = service.saveAuthType(authType, token);

            return ResponseEntity.ok(Collections.singletonMap("message", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Unexpected error: " + e.getMessage()));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<AuthTypes>> getAllAuthTypes() {
        return ResponseEntity.ok(service.getAllAuthTypes());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthTypeById(@PathVariable Long id) {
        return service.getAuthTypeById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Auth type not found")));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthType(@PathVariable Long id,
                                            @Valid @RequestBody AuthTypes updatedAuthType,
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
            String response = service.updateAuthType(id, updatedAuthType, token);

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
    public ResponseEntity<?> deleteAuthType(@PathVariable Long id,
                                            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String response = service.deleteAuthType(id, token);

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
