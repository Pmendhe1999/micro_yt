package com.identity.controller;

import com.identity.dto.CountryDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.Country;
import com.identity.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    @Autowired
    private CountryService service;

    private static final Logger log = LoggerFactory.getLogger(CountryController.class);

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<ResponceData> create(
            @Valid @RequestBody CountryDTO dto,
            BindingResult result,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
                return ResponseEntity.badRequest()
                        .body(new ResponceData("fail", 400, "Validation failed", errors, errors.size()));
            }

            String token = authHeader.replace("Bearer ", "");
            Country saved = service.save(dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "Country created successfully", saved, 1));

        } catch (Exception e) {
            log.error("Error creating Country: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }

    // ✅ READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "countryId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<Country> result = service.getAll(search, pageable);

            return ResponseEntity.ok(new ResponceData("success", 200, "Fetched countries",
                    result.getContent(), (int) result.getTotalElements()));

        } catch (Exception e) {
            log.error("Error fetching countries: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getById(@PathVariable Long id) {
        try {
            Optional<Country> entity = service.getById(id);
            return entity.map(country ->
                            ResponseEntity.ok(new ResponceData("success", 200, "Country fetched",
                                    Collections.singletonList(country), 1)))
                    .orElseGet(() ->
                            ResponseEntity.status(HttpStatus.NOT_FOUND)
                                    .body(new ResponceData("fail", 404, "Country not found", Collections.emptyList(), 0)));
        } catch (Exception e) {
            log.error("Error fetching Country: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> update(
            @PathVariable Long id,
            @Valid @RequestBody CountryDTO dto,
            BindingResult result,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(new ResponceData("fail", 400, "Validation errors", Collections.emptyList(), 0));
            }

            String token = authHeader.replace("Bearer ", "");
            Country updated = service.update(id, dto, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "Country updated successfully",
                    Collections.singletonList(updated), 1));

        } catch (Exception e) {
            log.error("Error updating Country: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            Country deleted = service.delete(id, token);
            return ResponseEntity.ok(new ResponceData("success", 200, "Country deleted successfully",
                    Collections.singletonList(deleted), 1));

        } catch (Exception e) {
            log.error("Error deleting Country: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ResponceData("fail", 500, e.getMessage(), Collections.emptyList(), 0));
        }
    }

    // ✅ PATCH
    @PatchMapping("/{id}")
    public ResponseEntity<ResponceData> patch(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No field to update");
        }

        Map.Entry<String, Object> entry = updates.entrySet().iterator().next();
        Country updated = service.patchCountry(id, entry.getKey(), entry.getValue());

        return ResponseEntity.ok(new ResponceData(
                "success", 200, "Country patched successfully",
                Collections.singletonList(updated), 1));
    }
}
