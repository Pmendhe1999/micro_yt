package com.qc.QcService.controllers;

import com.qc.QcService.dto.ProductDTO;
import com.qc.QcService.dto.ResponceData;
import com.qc.QcService.entities.Product;
import com.qc.QcService.services.ProductService;
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
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ResponceData> createProduct(
            @Valid @RequestBody ProductDTO dto,
            BindingResult result,
            @RequestHeader("Authorization") String authHeader) {

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
        }

        try {
            String token = authHeader.replace("Bearer ", "");
            Product saved = productService.saveProduct(dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "Product created", saved, 1));
        } catch (Exception e) {
            log.error("Error creating Product: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), null, 0));
        }
    }

    @GetMapping
    public ResponseEntity<ResponceData> getAllProducts(
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
            Page<Product> result = productService.getAllProducts(search, pageable);

            return ResponseEntity.ok(new ResponceData("success", 200, "Fetched Products", result.getContent(), (int) result.getTotalElements()));
        } catch (Exception e) {
            log.error("Error fetching Products: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.getProductById(id);
            return product.map(value ->
                            ResponseEntity.ok(new ResponceData("success", 200, "Fetched Product", List.of(value), 1)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponceData("fail", 404, "Product not found", Collections.emptyList(), 0)));
        } catch (Exception e) {
            log.error("Error fetching Product: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO dto,
            BindingResult result,
            @RequestHeader("Authorization") String authHeader) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponceData("fail", 400, "Validation errors", Collections.emptyList(), 0));
        }

        try {
            String token = authHeader.replace("Bearer ", "");
            Product updated = productService.updateProduct(id, dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "Product updated", List.of(updated), 1));
        } catch (Exception e) {
            log.error("Error updating Product: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            Product deleted = productService.deleteProduct(id, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "Product deleted", List.of(deleted), 1));
        } catch (Exception e) {
            log.error("Error deleting Product: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }
}
