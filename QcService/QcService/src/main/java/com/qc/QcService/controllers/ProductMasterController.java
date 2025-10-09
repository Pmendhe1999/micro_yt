package com.qc.QcService.controllers;

import com.qc.QcService.dto.ProductMasterDTO;
import com.qc.QcService.dto.ResponceData;
import com.qc.QcService.entities.ProductMaster;
import com.qc.QcService.services.ProductMasterService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductMasterController {

    @Autowired
    private ProductMasterService service;

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createProduct(
            @Valid @RequestBody ProductMasterDTO dto,
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
            ProductMaster saved = service.saveProduct(dto, token);
            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Product created successfully", saved, 1));

        } catch (Exception e) {
            log.error("Error creating Product: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            Page<ProductMaster> result = service.getAllProducts(search, pageable);

            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Retrieved Products", result.getContent(),
                            (int) result.getTotalElements()));
        } catch (Exception e) {
            log.error("Error fetching Products: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getProductById(@PathVariable Long id) {
        try {
            Optional<ProductMaster> product = service.getProductById(id);
            if (product.isPresent()) {
                return ResponseEntity.ok(
                        new ResponceData("success", 200, "Retrieved Product", List.of(product.get()), 1));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponceData("fail", 404, "Product not found", Collections.emptyList(), 0));
            }
        } catch (Exception e) {
            log.error("Error fetching Product by id: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductMasterDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(new ResponceData("fail", 400, "Validation errors", Collections.emptyList(), 0));
            }

            String token = authHeader.replace("Bearer ", "");
            ProductMaster updated = service.updateProduct(id, dto, token);
            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Product updated successfully", List.of(updated), 1));

        } catch (Exception e) {
            log.error("Error updating Product: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            ProductMaster deleted = service.deleteProduct(id, token);
            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Product deleted successfully", List.of(deleted), 1));

        } catch (Exception e) {
            log.error("Error deleting Product: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }
}
