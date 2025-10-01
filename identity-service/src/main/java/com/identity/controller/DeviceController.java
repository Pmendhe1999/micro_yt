package com.identity.controller;

import com.identity.dto.DeviceDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.Device;
import com.identity.service.DeviceService;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService service;

    private static final Logger log = LoggerFactory.getLogger(DeviceController.class);

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createDevice(
            @Valid @RequestBody DeviceDTO dto,
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
            Device saved = service.saveDevice(dto, token);

            return ResponseEntity.ok(new ResponceData("success", 200, "Device created successfully", saved, 1));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponceData("error", 500, "Unexpected error: " + e.getMessage(), null, 0));
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllDevices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            Page<Device> result = service.getAllDevices(search, pageable);

            return ResponseEntity.ok(new ResponceData("success", 200, "Retrieved Devices", result.getContent(), (int) result.getTotalElements()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getDeviceById(@PathVariable Long id) {
        Optional<Device> device = service.getDeviceById(id);
        return device.map(value ->
                        ResponseEntity.ok(new ResponceData("success", 200, "Device retrieved", value, 1)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponceData("fail", 404, "Device not found with id " + id, null, 0)));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateDevice(
            @PathVariable Long id,
            @Valid @RequestBody DeviceDTO dto,
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
            Device updated = service.updateDeviceReturnEntity(id, dto, token);

            return ResponseEntity.ok(new ResponceData("success", 200, "Device updated successfully", updated, 1));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), null, 0));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteDevice(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Device deleted = service.deleteDeviceReturnEntity(id, token);

            return ResponseEntity.ok(new ResponceData("success", 200, "Device deleted successfully", deleted, 1));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponceData("fail", 500, "Unexpected error: " + e.getMessage(), null, 0));
        }
    }
}
