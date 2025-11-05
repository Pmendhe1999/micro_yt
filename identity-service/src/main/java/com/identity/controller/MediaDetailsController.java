package com.identity.controller;

import com.identity.dto.MediaDetailsDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.MediaDetails;
import com.identity.service.MediaDetailsService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/identity/media-details")
public class MediaDetailsController {
    @Autowired
    private MediaDetailsService service;

    private static final Logger log = LoggerFactory.getLogger(MediaDetailsController.class);

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createMediaDetails(
            @Valid @RequestBody MediaDetailsDTO dto,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Creating MediaDetails: {}", dto);
            String token = authHeader.replace("Bearer ", "");
            MediaDetails saved = service.saveMediaDetails(dto, token);

            ResponceData response = new ResponceData("success", 200,
                    "MediaDetails created successfully", saved, 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error while creating MediaDetails: {}", e.getMessage(), e);
            ResponceData response = new ResponceData("fail", 500,
                    "Unexpected error: " + e.getMessage(), null, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllMediaDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<MediaDetails> result = service.getAllMediaDetails(search, pageable);
            ResponceData response = new ResponceData("success", 200,
                    "Retrieved MediaDetails", result.getContent(), (int) result.getTotalElements());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error while fetching MediaDetails: {}", e.getMessage(), e);
            ResponceData response = new ResponceData("fail", 500,
                    "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getMediaDetailsById(@PathVariable Long id) {
        try {
            Optional<MediaDetails> details = service.getMediaDetailsById(id);

            if (details.isPresent()) {
                ResponceData response = new ResponceData("success", 200,
                        "Retrieved MediaDetails", Collections.singletonList(details.get()), 1);
                return ResponseEntity.ok(response);
            } else {
                ResponceData response = new ResponceData("fail", 404,
                        "MediaDetails not found with id " + id, Collections.emptyList(), 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            log.error("Error while fetching MediaDetails id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData("fail", 500,
                    "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateMediaDetails(
            @PathVariable Long id,
            @Valid @RequestBody MediaDetailsDTO dto,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            MediaDetails updated = service.updateMediaDetailsReturnEntity(id, dto, token);

            ResponceData response = new ResponceData("success", 200,
                    "MediaDetails updated successfully", Collections.singletonList(updated), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error while updating MediaDetails id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData("fail", 500,
                    "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteMediaDetails(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            MediaDetails deleted = service.deleteMediaDetailsReturnEntity(id, token);

            ResponceData response = new ResponceData("success", 200,
                    "MediaDetails deleted successfully", Collections.singletonList(deleted), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error while deleting MediaDetails id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData("fail", 500,
                    "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // GET BY USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponceData> getByUserId(@PathVariable Long userId) {
        try {
            List<MediaDetails> list = service.getMediaDetailsByUserId(userId);

            if (list.isEmpty()) {
                ResponceData response = new ResponceData("fail", 404,
                        "No MediaDetails found for userId " + userId, Collections.emptyList(), 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            ResponceData response = new ResponceData("success", 200,
                    "Retrieved MediaDetails for userId " + userId, list, list.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error while fetching MediaDetails for userId={}: {}", userId, e.getMessage(), e);
            ResponceData response = new ResponceData("fail", 500,
                    "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // GET BY MEDIA
    @GetMapping("/media/{mediaId}")
    public ResponseEntity<ResponceData> getByMediaId(@PathVariable Long mediaId) {
        try {
            List<MediaDetails> list = service.getMediaDetailsByMediaId(mediaId);

            if (list.isEmpty()) {
                ResponceData response = new ResponceData("fail", 404,
                        "No MediaDetails found for mediaId " + mediaId, Collections.emptyList(), 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            ResponceData response = new ResponceData("success", 200,
                    "Retrieved MediaDetails for mediaId " + mediaId, list, list.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error while fetching MediaDetails for mediaId={}: {}", mediaId, e.getMessage(), e);
            ResponceData response = new ResponceData("fail", 500,
                    "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
