package com.qc.controllers;

import com.qc.dto.QuantitativeCheckDTO;
import com.qc.dto.QuantitativeCheckDTOResponse;
import com.qc.dto.Response;
import com.qc.exception.IllegalArgumentsException;
import com.qc.services.QuantitativeCheckService;
import com.qc.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qc")
@Slf4j
public class QuantitativeCheckController {

    @Autowired
    private QuantitativeCheckService quantitativeCheckService;

    @Autowired
    private ResponseService responseService;

    @Operation(summary = "Create a List of Quantitative Checks")
    @PostMapping("/quantitativeCheck/all")
    public ResponseEntity<Response<Void>> createAll(@Validated @RequestBody List<QuantitativeCheckDTO> dtoList) {
        log.info("Request to create QuantitativeCheck list");
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentsException("Quantitative Check list cannot be empty");
        }
        quantitativeCheckService.createAllQuantitativeCheck(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Quantitative Checks created successfully", null, 0);
    }

    @Operation(summary = "Create a Quantitative Check")
    @PostMapping("/quantitativeCheck")
    public ResponseEntity<Response<Void>> create(@Validated @RequestBody QuantitativeCheckDTO dto) {
        log.info("Request to create QuantitativeCheck: {}", dto.getDescription());
        quantitativeCheckService.createQuantitativeCheck(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Quantitative Check created successfully", null, 0);
    }

    @Operation(summary = "List all Quantitative Checks with filters and pagination")
    @GetMapping("/quantitativeCheck")
    public ResponseEntity<Response<List<QuantitativeCheckDTOResponse>>> getAllQuantitativeChecks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean isScan,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String value,
            @RequestParam(required = false) List<Long> quantitativeCheckMasterIds,
            @RequestParam(required = false) List<Long> productIds, // ✅ new filter
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<QuantitativeCheckDTOResponse> result =
                quantitativeCheckService.getAllQuantitativeChecksWithFilters(
                        description, isScan, status, value, quantitativeCheckMasterIds, productIds, pageable);

        return responseService.success(HttpStatus.OK.value(),
                "Quantitative Checks fetched successfully",
                result.getContent(),
                result.getTotalElements());
    }


    @Operation(summary = "Get Quantitative Check by ID")
    @GetMapping("/quantitativeCheck/{id}")
    public ResponseEntity<Response<QuantitativeCheckDTOResponse>> getById(@PathVariable Long id) {
        QuantitativeCheckDTOResponse response = quantitativeCheckService.getQuantitativeCheckById(id);
        return responseService.success(HttpStatus.OK.value(), "Quantitative Check fetched successfully", response, 1);
    }

    @Operation(summary = "Update Quantitative Check by ID")
    @PutMapping("/quantitativeCheck/{id}")
    public ResponseEntity<Response<QuantitativeCheckDTOResponse>> update(@PathVariable Long id,
                                                                         @Validated @RequestBody QuantitativeCheckDTO dto) {
        QuantitativeCheckDTOResponse updated = quantitativeCheckService.updateQuantitativeCheck(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Quantitative Check updated successfully", updated, 1);
    }

    @Operation(summary = "Partially update Quantitative Check by ID")
    @PatchMapping("/quantitativeCheck/{id}")
    public ResponseEntity<Response<QuantitativeCheckDTOResponse>> patch(@PathVariable Long id,
                                                                        @RequestBody QuantitativeCheckDTO dto) {
        QuantitativeCheckDTOResponse updated = quantitativeCheckService.patchQuantitativeCheck(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Quantitative Check patched successfully", updated, 1);
    }

    @Operation(summary = "Delete Quantitative Check by ID")
    @DeleteMapping("/quantitativeCheck/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        quantitativeCheckService.deleteQuantitativeCheck(id);
        return responseService.success(HttpStatus.OK.value(), "Quantitative Check deleted successfully", null, 0);
    }
}
