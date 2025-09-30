package com.identity.controller;

import com.identity.dto.NotificationTechDetailsDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.NotificationTechDetails;
import com.identity.service.NotificationTechDetailsService;
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
@RequestMapping("/notification-tech-details")
public class NotificationTechDetailsController {
    @Autowired
    private NotificationTechDetailsService service;

    private static final Logger log = LoggerFactory.getLogger(NotificationTechDetailsController.class);

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> create(
            @Valid @RequestBody NotificationTechDetailsDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity.badRequest()
                        .body(new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            NotificationTechDetails saved = service.save(dto, token);

            log.info("Created NotificationTechDetails with ID {}", saved.getId());
            return ResponseEntity.ok(new ResponceData("success", 200, "Created successfully", saved, 1));
        } catch (Exception e) {
            log.error("Unexpected error while creating NotificationTechDetails: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(),
                            Collections.emptyList(), 0));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAll(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(required = false) String search,
                                               @RequestParam(defaultValue = "id") String sortBy,
                                               @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            log.info("Fetching NotificationTechDetails page={}, size={}, search={}, sortBy={}, sortDir={}",
                    page, size, search, sortBy, sortDir);

            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page-1, size, sort);

            Page<NotificationTechDetails> result = service.getAll(search, pageable);

            log.info("Retrieved {} NotificationTechDetails", result.getTotalElements());
            return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved successfully",
                    result.getContent(), (int) result.getTotalElements()));
        } catch (Exception e) {
            log.error("Unexpected error while fetching NotificationTechDetails: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(),
                            Collections.emptyList(), 0));
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getById(@PathVariable Long id) {
        try {
            log.info("Fetching NotificationTechDetails by ID {}", id);
            Optional<NotificationTechDetails> entity = service.getById(id);

            return entity.map(value -> ResponseEntity.ok(
                            new ResponceData("success", 200, "Retrieved successfully", value, 1)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponceData("fail", 404, "Not found", null, 0)));
        } catch (Exception e) {
            log.error("Unexpected error while fetching NotificationTechDetails by ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(),
                            Collections.emptyList(), 0));
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> update(@PathVariable Long id,
                                               @Valid @RequestBody NotificationTechDetailsDTO dto,
                                               BindingResult bindingResult,
                                               @RequestHeader("Authorization") String authHeader) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity.badRequest()
                        .body(new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            NotificationTechDetails updated = service.updateReturnEntity(id, dto, token);

            log.info("Updated NotificationTechDetails with ID {}", id);
            return ResponseEntity.ok(new ResponceData("success", 200, "Updated successfully", updated, 1));
        } catch (Exception e) {
            log.error("Unexpected error while updating NotificationTechDetails with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(),
                            Collections.emptyList(), 0));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> delete(@PathVariable Long id,
                                               @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            NotificationTechDetails deleted = service.deleteReturnEntity(id, token);

            log.info("Deleted NotificationTechDetails with ID {}", id);
            return ResponseEntity.ok(new ResponceData("success", 200, "Deleted successfully", deleted, 1));
        } catch (Exception e) {
            log.error("Unexpected error while deleting NotificationTechDetails with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(),
                            Collections.emptyList(), 0));
        }
    }

    // GET BY SERVICE PROVIDER
    @GetMapping("/service-provider/{spmId}")
    public ResponseEntity<ResponceData> getByServiceProvider(@PathVariable Long spmId) {
        try {
            log.info("Fetching NotificationTechDetails for ServiceProviderMaster ID {}", spmId);
            List<NotificationTechDetails> list = service.getByServiceProviderMasterId(spmId);

            return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved successfully", list, list.size()));
        } catch (Exception e) {
            log.error("Unexpected error while fetching NotificationTechDetails for ServiceProviderMaster ID {}: {}", spmId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(),
                            Collections.emptyList(), 0));
        }
    }
}
