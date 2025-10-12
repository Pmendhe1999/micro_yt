package com.qc.QcService.controllers;

import com.qc.QcService.dto.LabelScanMasterDTO;
import com.qc.QcService.dto.ResponceData;
import com.qc.QcService.entities.LabelScanMaster;
import com.qc.QcService.services.LabelScanMasterService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/label-scan")
public class LabelScanMasterController {

    @Autowired
    private LabelScanMasterService service;

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createLabel(
            @Valid @RequestBody LabelScanMasterDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(
                        new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            LabelScanMaster saved = service.saveLabel(dto, token);
            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Label created successfully", saved, 1));

        } catch (Exception e) {
            log.error("Error creating Label: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllLabels(
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

            Page<LabelScanMaster> result = service.getAllLabels(search, pageable);

            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Retrieved Labels", result.getContent(),
                            (int) result.getTotalElements()));
        } catch (Exception e) {
            log.error("Error fetching Labels: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getLabelById(@PathVariable Long id) {
        try {
            Optional<LabelScanMaster> label = service.getLabelById(id);
            if (label.isPresent()) {
                return ResponseEntity.ok(
                        new ResponceData("success", 200, "Retrieved Label", List.of(label.get()), 1));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponceData("fail", 404, "Label not found", Collections.emptyList(), 0));
            }
        } catch (Exception e) {
            log.error("Error fetching Label by id: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateLabel(
            @PathVariable Long id,
            @Valid @RequestBody LabelScanMasterDTO dto,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(new ResponceData("fail", 400, "Validation errors", Collections.emptyList(), 0));
            }

            String token = authHeader.replace("Bearer ", "");
            LabelScanMaster updated = service.updateLabel(id, dto, token);
            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Label updated successfully", List.of(updated), 1));

        } catch (Exception e) {
            log.error("Error updating Label: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteLabel(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            LabelScanMaster deleted = service.deleteLabel(id, token);
            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Label deleted successfully", List.of(deleted), 1));

        } catch (Exception e) {
            log.error("Error deleting Label: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }
}
