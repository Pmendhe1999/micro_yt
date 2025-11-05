package com.identity.controller;

import com.identity.dto.MediaDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.Media;
import com.identity.entity.MediaDetails;
import com.identity.service.MediaService;
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
import org.springframework.web.multipart.MultipartFile;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/identity/media")
public class MediaController {

    @Autowired
    private MediaService service;

    private static final Logger log = LoggerFactory.getLogger(MediaController.class);

    @PostMapping
    public ResponseEntity<ResponceData> createMedia(
            @Valid @RequestBody MediaDTO mediaDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
                ResponceData response = new ResponceData("fail", 400, "Validation failed", errors, errors.size());
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            Media saved = service.saveMedia(mediaDTO, token);

            ResponceData response = new ResponceData("success", 200, "Media created successfully", saved, 1);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ResponceData response = new ResponceData("fail", 400, e.getMessage(), null, 0);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ResponceData response = new ResponceData("error", 500, "Unexpected error: " + e.getMessage(), null, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ResponceData> getAllMedia(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = (Pageable) PageRequest.of(page - 1, size, sort);
            Page<Media> result = service.getAllMedia(search, pageable);

            ResponceData response = new ResponceData("success", 200, "Retrieved Media", result.getContent(), (int) result.getTotalElements());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponceData response = new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getMediaById(@PathVariable Long id) {
        try {
            Optional<Media> media = service.getMediaById(id);
            if (media.isPresent()) {
                ResponceData response = new ResponceData("success", 200, "Retrieved Media", Collections.singletonList(media.get()), 1);
                return ResponseEntity.ok(response);
            } else {
                ResponceData response = new ResponceData("fail", 404, "Media not found with id " + id, Collections.emptyList(), 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ResponceData response = new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateMedia(
            @PathVariable Long id,
            @Valid @RequestBody MediaDTO mediaDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
                ResponceData response = new ResponceData("fail", 400, "Validation errors", errors, errors.size());
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            Media updated = service.updateMediaReturnEntity(id, mediaDTO, token);
            ResponceData response = new ResponceData("success", 200, "Media updated successfully", Collections.singletonList(updated), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponceData response = new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteMedia(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Media deleted = service.deleteMediaReturnEntity(id, token);
            ResponceData response = new ResponceData("success", 200, "Media deleted successfully", Collections.singletonList(deleted), 1);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponceData response = new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // ✅ Existing user upload endpoint
    @PostMapping("/upload")
    public ResponseEntity<MediaDetails> uploadUserMedia(
            @RequestParam Long userId,
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String mediaFor) {
        return ResponseEntity.ok(service.uploadMedia(userId, null, file, description, mediaFor));
    }

    // ✅ New endpoint for application media upload
    @PostMapping("/upload/application")
    public ResponseEntity<MediaDetails> uploadApplicationMedia(
            @RequestParam Long applicationId,
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String mediaFor) {
        return ResponseEntity.ok(service.uploadMedia(null, applicationId, file, description, mediaFor));
    }


}
