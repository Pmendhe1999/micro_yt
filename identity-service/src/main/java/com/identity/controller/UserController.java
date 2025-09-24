package com.identity.controller;

import com.identity.dto.ResponceData;
import com.identity.dto.UserRegisterDto;
import com.identity.entity.UserCredential;
import com.identity.service.UserService;
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
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    // CREATE
    @PostMapping("/register")
    public ResponseEntity<ResponceData> registerUser(
            @Valid @RequestBody UserRegisterDto dto,
            BindingResult bindingResult) {
        try {
            log.info("Received request to create User: {}", dto);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for UserRegisterDto: {}", errors);

                ResponceData response = new ResponceData(
                        "fail", 400, "Validation failed", errors, errors.size());
                return ResponseEntity.badRequest().body(response);
            }


            UserCredential savedUser = userService.saveUser(dto);

            log.info("User created successfully with id={} and username={}",
                    savedUser.getUserId(), savedUser.getUsername());

            ResponceData response = new ResponceData(
                    "success", 200, "User created successfully",
                    Collections.singletonList(savedUser), 1);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Failed to create User: {}", e.getMessage());
            ResponceData response = new ResponceData(
                    "fail", 400, e.getMessage(), null, 0);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Unexpected error while creating User: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "error", 500, "Unexpected error: " + e.getMessage(), null, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            log.info("Fetching Users page={}, size={}, search={}, sortBy={}, sortDir={}",
                    page, size, search, sortBy, sortDir);

            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page-1, size, sort);

            Page<UserCredential> result = userService.getAllUsers(search, pageable);

            log.info("Retrieved {} Users", result.getTotalElements());

            ResponceData response = new ResponceData(
                    "success", 200, "Retrieved Users",
                    result.getContent(), (int) result.getTotalElements());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while fetching Users: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getUserById(@PathVariable Long id) {
        try {
            log.info("Fetching User by id={}", id);

            Optional<UserCredential> user = userService.getUserById(id);

            if (user.isPresent()) {
                log.info("Found User with id={}", id);
                ResponceData response = new ResponceData(
                        "success", 200, "Retrieved User",
                        Collections.singletonList(user.get()), 1);
                return ResponseEntity.ok(response);
            } else {
                log.warn("User not found with id={}", id);
                ResponceData response = new ResponceData(
                        "fail", 404, "User not found with id " + id,
                        Collections.emptyList(), 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            log.error("Unexpected error while fetching User id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRegisterDto dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Updating User id={} with payload={}", id, dto);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for update User id={}, errors={}", id, errors);

                ResponceData response = new ResponceData(
                        "fail", 400, "Validation errors", errors, errors.size());
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            UserCredential updatedUser = userService.updateUser(id, dto, token);

            log.info("User updated successfully id={}", updatedUser.getUserId());

            ResponceData response = new ResponceData(
                    "success", 200, "User updated successfully",
                    Collections.singletonList(updatedUser), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while updating User id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteUser(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Deleting User id={}", id);

            String token = authHeader.replace("Bearer ", "");
            UserCredential deletedUser = userService.deleteUser(id, token);

            log.info("User deleted successfully id={}", deletedUser.getUserId());

            ResponceData response = new ResponceData(
                    "success", 200, "User deleted successfully",
                    Collections.singletonList(deletedUser), 1);
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            log.warn("User not found for deletion id={}", id);
            ResponceData response = new ResponceData(
                    "fail", 404, e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("Unexpected error while deleting User id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
