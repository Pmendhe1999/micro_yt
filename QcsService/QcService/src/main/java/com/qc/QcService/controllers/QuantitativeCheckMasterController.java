package com.qc.QcService.controllers;

import com.qc.QcService.dto.QuantitativeCheckMasterDTO;
import com.qc.QcService.dto.ResponceData;
import com.qc.QcService.entities.QuantitativeCheckMaster;
import com.qc.QcService.services.QuantitativeCheckMasterService;
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
@RequestMapping("/quantitative-checks")
public class QuantitativeCheckMasterController {

    private static final Logger log = LoggerFactory.getLogger(QuantitativeCheckMasterController.class);

    @Autowired
    private QuantitativeCheckMasterService service;

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createCheck(
            @Valid @RequestBody QuantitativeCheckMasterDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
                return ResponseEntity.badRequest().body(
                        new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            QuantitativeCheckMaster saved = service.saveCheck(dto, token);

            return ResponseEntity.ok(
                    new ResponceData("success", 200, "QuantitativeCheck created successfully", saved, 1));

        } catch (Exception e) {
            log.error("Error creating QuantitativeCheck: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllChecks(
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
            Page<QuantitativeCheckMaster> result = service.getAllChecks(search, pageable);

            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Fetched QuantitativeChecks", result.getContent(),
                            (int) result.getTotalElements()));

        } catch (Exception e) {
            log.error("Error fetching QuantitativeChecks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getCheckById(@PathVariable Long id) {
        try {
            Optional<QuantitativeCheckMaster> check = service.getCheckById(id);
            if (check.isPresent()) {
                return ResponseEntity.ok(
                        new ResponceData("success", 200, "Fetched QuantitativeCheck", List.of(check.get()), 1));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponceData("fail", 404, "Check not found", Collections.emptyList(), 0));
            }
        } catch (Exception e) {
            log.error("Error fetching QuantitativeCheck by id: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateCheck(
            @PathVariable Long id,
            @Valid @RequestBody QuantitativeCheckMasterDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(new ResponceData("fail", 400, "Validation errors", Collections.emptyList(), 0));
            }

            String token = authHeader.replace("Bearer ", "");
            QuantitativeCheckMaster updated = service.updateCheck(id, dto, token);

            return ResponseEntity.ok(
                    new ResponceData("success", 200, "QuantitativeCheck updated successfully", List.of(updated), 1));

        } catch (Exception e) {
            log.error("Error updating QuantitativeCheck: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteCheck(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            QuantitativeCheckMaster deleted = service.deleteCheck(id, token);

            return ResponseEntity.ok(
                    new ResponceData("success", 200, "QuantitativeCheck deleted successfully", List.of(deleted), 1));

        } catch (Exception e) {
            log.error("Error deleting QuantitativeCheck: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }
}
