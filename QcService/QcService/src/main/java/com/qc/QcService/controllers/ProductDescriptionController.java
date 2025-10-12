package com.qc.QcService.controllers;

import com.qc.QcService.dto.ProductDescriptionDTO;
import com.qc.QcService.dto.ResponceData;
import com.qc.QcService.entities.ProductDescription;
import com.qc.QcService.services.ProductDescriptionService;
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
@RequestMapping("/product-description")
@RequiredArgsConstructor
public class ProductDescriptionController {

    private static final Logger log = LoggerFactory.getLogger(ProductDescriptionController.class);

    @Autowired
    private ProductDescriptionService service;

    @PostMapping
    public ResponseEntity<ResponceData> createDescription(
            @Valid @RequestBody ProductDescriptionDTO dto,
            BindingResult result,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
                return ResponseEntity.badRequest().body(new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            ProductDescription saved = service.saveDescription(dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "ProductDescription created", saved, 1));

        } catch (Exception e) {
            log.error("Error creating ProductDescription: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }

    @GetMapping
    public ResponseEntity<ResponceData> getAllDescriptions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<ProductDescription> result = service.getAllDescriptions(search, pageable);

            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Fetched ProductDescriptions", result.getContent(), (int) result.getTotalElements()));

        } catch (Exception e) {
            log.error("Error fetching ProductDescriptions: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getById(@PathVariable Long id) {
        try {
            Optional<ProductDescription> desc = service.getDescriptionById(id);
            return desc.map(value ->
                            ResponseEntity.ok(new ResponceData("success", 200, "Fetched ProductDescription", List.of(value), 1)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponceData("fail", 404, "ProductDescription not found", Collections.emptyList(), 0)));
        } catch (Exception e) {
            log.error("Error fetching ProductDescription: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateDescription(
            @PathVariable Long id,
            @Valid @RequestBody ProductDescriptionDTO dto,
            BindingResult result,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(new ResponceData("fail", 400, "Validation errors", Collections.emptyList(), 0));
            }

            String token = authHeader.replace("Bearer ", "");
            ProductDescription updated = service.updateDescription(id, dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "ProductDescription updated", List.of(updated), 1));

        } catch (Exception e) {
            log.error("Error updating ProductDescription: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteDescription(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            ProductDescription deleted = service.deleteDescription(id, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "ProductDescription deleted", List.of(deleted), 1));

        } catch (Exception e) {
            log.error("Error deleting ProductDescription: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }
}
