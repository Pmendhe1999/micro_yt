package com.qc.QcService.controllers;

import com.qc.QcService.dto.DeliveryChallanMasterDTO;
import com.qc.QcService.dto.ResponceData;
import com.qc.QcService.entities.DeliveryChallanMaster;
import com.qc.QcService.services.DeliveryChallanMasterService;
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
@RequestMapping("/delivery-challan-master")
@RequiredArgsConstructor
public class DeliveryChallanMasterController {

    private static final Logger log = LoggerFactory.getLogger(DeliveryChallanMasterController.class);

    @Autowired
    private DeliveryChallanMasterService service;

    @PostMapping
    public ResponseEntity<ResponceData> createMaster(@Valid @RequestBody DeliveryChallanMasterDTO dto,
                                                     BindingResult result,
                                                     @RequestHeader("Authorization") String authHeader) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
                return ResponseEntity.badRequest().body(new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            DeliveryChallanMaster saved = service.saveMaster(dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "DeliveryChallanMaster created", saved, 1));

        } catch (Exception e) {
            log.error("Error creating DeliveryChallanMaster: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), null, 0));
        }
    }

    @GetMapping
    public ResponseEntity<ResponceData> getAllMasters(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(required = false) String search,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<DeliveryChallanMaster> result = service.getAllMasters(search, pageable);
            return ResponseEntity.ok(new ResponceData("success", 200, "Fetched DeliveryChallanMasters", result.getContent(), (int) result.getTotalElements()));
        } catch (Exception e) {
            log.error("Error fetching DeliveryChallanMasters: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getById(@PathVariable Long id) {
        try {
            Optional<DeliveryChallanMaster> master = service.getMasterById(id);
            return master.map(value -> ResponseEntity.ok(new ResponceData("success", 200, "Fetched DeliveryChallanMaster", List.of(value), 1)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponceData("fail", 404, "DeliveryChallanMaster not found", Collections.emptyList(), 0)));
        } catch (Exception e) {
            log.error("Error fetching DeliveryChallanMaster: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateMaster(@PathVariable Long id,
                                                     @Valid @RequestBody DeliveryChallanMasterDTO dto,
                                                     BindingResult result,
                                                     @RequestHeader("Authorization") String authHeader) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(new ResponceData("fail", 400, "Validation errors", Collections.emptyList(), 0));
            }

            String token = authHeader.replace("Bearer ", "");
            DeliveryChallanMaster updated = service.updateMaster(id, dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "DeliveryChallanMaster updated", List.of(updated), 1));

        } catch (Exception e) {
            log.error("Error updating DeliveryChallanMaster: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteMaster(@PathVariable Long id,
                                                     @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            DeliveryChallanMaster deleted = service.deleteMaster(id, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "DeliveryChallanMaster deleted", List.of(deleted), 1));
        } catch (Exception e) {
            log.error("Error deleting DeliveryChallanMaster: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }
}
