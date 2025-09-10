package com.identity.controller;

import com.identity.dto.ProjectRegisterDto;
import com.identity.entity.ProjectRegistrationMaster;
import com.identity.service.ProjectRegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/projects")
public class ProjectRegistrationController {

    @Autowired
    private ProjectRegistrationService service;


    // CREATE
    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectRegisterDto dto,
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
            String response = service.saveProject(dto, token);

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
    public ResponseEntity<List<ProjectRegistrationMaster>> getAllProjects() {
        return ResponseEntity.ok(service.getAllProjects());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        return service.getProjectById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Project not found")));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id,
                                           @Valid @RequestBody ProjectRegistrationMaster updatedProject,
                                           @RequestParam(required = false) Long authTypeId,
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
            String response = service.updateProject(id, updatedProject, authTypeId, token);

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
    public ResponseEntity<?> deleteProject(@PathVariable Long id,
                                           @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String response = service.deleteProject(id, token);

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
