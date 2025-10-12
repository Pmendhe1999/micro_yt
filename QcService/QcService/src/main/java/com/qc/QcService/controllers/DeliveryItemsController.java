package com.qc.QcService.controllers;

import com.qc.QcService.dto.DeliveryItemsDTO;
import com.qc.QcService.dto.ResponceData;
import com.qc.QcService.entities.DeliveryItems;
import com.qc.QcService.services.DeliveryItemsService;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/delivery-items")
@RequiredArgsConstructor
public class DeliveryItemsController {


    private static final Logger log = LoggerFactory.getLogger(DeliveryItemsController.class);

    @Autowired
    private DeliveryItemsService service;

    @PostMapping
    public ResponseEntity<ResponceData> createItem(
            @Valid @RequestBody DeliveryItemsDTO dto,
            BindingResult result,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
                return ResponseEntity.badRequest().body(new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }
            String token = authHeader.replace("Bearer ", "");
            DeliveryItems saved = service.saveItem(dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "DeliveryItem created", saved, 1));
        } catch (Exception e) {
            log.error("Error creating DeliveryItem: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }

    @GetMapping
    public ResponseEntity<ResponceData> getAllItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<DeliveryItems> result = service.getAllItems(search, pageable);
            return ResponseEntity.ok(new ResponceData("success", 200, "Fetched DeliveryItems", result.getContent(), (int) result.getTotalElements()));
        } catch (Exception e) {
            log.error("Error fetching DeliveryItems: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getById(@PathVariable Long id) {
        try {
            return service.getItemById(id)
                    .map(value -> ResponseEntity.ok(new ResponceData("success", 200, "Fetched DeliveryItem", List.of(value), 1)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponceData("fail", 404, "DeliveryItem not found", Collections.emptyList(), 0)));
        } catch (Exception e) {
            log.error("Error fetching DeliveryItem: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody DeliveryItemsDTO dto,
            BindingResult result,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(new ResponceData("fail", 400, "Validation errors", Collections.emptyList(), 0));
            }
            String token = authHeader.replace("Bearer ", "");
            DeliveryItems updated = service.updateItem(id, dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "DeliveryItem updated", List.of(updated), 1));
        } catch (Exception e) {
            log.error("Error updating DeliveryItem: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteItem(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            DeliveryItems deleted = service.deleteItem(id, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "DeliveryItem deleted", List.of(deleted), 1));
        } catch (Exception e) {
            log.error("Error deleting DeliveryItem: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }
}
