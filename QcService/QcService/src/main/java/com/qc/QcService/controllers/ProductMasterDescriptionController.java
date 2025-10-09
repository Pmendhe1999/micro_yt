package com.qc.QcService.controllers;

import com.qc.QcService.dto.ProductMasterDescriptionDTO;
import com.qc.QcService.dto.ResponceData;
import com.qc.QcService.entities.ProductMasterDescription;
import com.qc.QcService.services.ProductMasterDescriptionService;
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
@RequestMapping("/product-descriptions")
public class ProductMasterDescriptionController {
    @Autowired
    private ProductMasterDescriptionService service;

    private static final Logger log = LoggerFactory.getLogger(ProductMasterDescriptionController.class);


    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createDescription(
            @Valid @RequestBody ProductMasterDescriptionDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(
                        new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            ProductMasterDescription saved = service.saveDescription(dto, token);
            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Description created successfully", saved, 1));

        } catch (Exception e) {
            log.error("Error creating Description: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllDescriptions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            Page<ProductMasterDescription> result = service.getAllDescriptions(search, pageable);

            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Retrieved Descriptions", result.getContent(),
                            (int) result.getTotalElements()));
        } catch (Exception e) {
            log.error("Error fetching Descriptions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getDescriptionById(@PathVariable Long id) {
        try {
            Optional<ProductMasterDescription> desc = service.getDescriptionById(id);
            if (desc.isPresent()) {
                return ResponseEntity.ok(
                        new ResponceData("success", 200, "Retrieved Description", List.of(desc.get()), 1));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponceData("fail", 404, "Description not found", Collections.emptyList(), 0));
            }
        } catch (Exception e) {
            log.error("Error fetching Description by id: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateDescription(
            @PathVariable Long id,
            @Valid @RequestBody ProductMasterDescriptionDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(new ResponceData("fail", 400, "Validation errors", Collections.emptyList(), 0));
            }

            String token = authHeader.replace("Bearer ", "");
            ProductMasterDescription updated = service.updateDescription(id, dto, token);
            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Description updated successfully", List.of(updated), 1));

        } catch (Exception e) {
            log.error("Error updating Description: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteDescription(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            ProductMasterDescription deleted = service.deleteDescription(id, token);
            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Description deleted successfully", List.of(deleted), 1));

        } catch (Exception e) {
            log.error("Error deleting Description: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }
}
