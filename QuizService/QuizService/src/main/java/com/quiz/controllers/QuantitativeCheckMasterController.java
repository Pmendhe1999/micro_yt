package com.quiz.controllers;

import com.quiz.dto.QuantitativeCheckMasterDTO;
import com.quiz.dto.QuantitativeCheckMasterDTOResponse;
import com.quiz.dto.Response;
import com.quiz.entities.QuantitativeCheckMaster;
import com.quiz.exception.IllegalArgumentsException;
import com.quiz.services.QuantitativeCheckMasterService;
import com.quiz.services.ResponseService;
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
public class QuantitativeCheckMasterController {
    @Autowired
    private ResponseService responseService;

    @Autowired
    private QuantitativeCheckMasterService quantitativeCheckMasterService;

    @Operation(summary = "Create a list of Quantitative Check Masters")
    @PostMapping("/quantitativeCheckMaster/all")
    public ResponseEntity<Response<Void>> createAll(@Validated @RequestBody List<QuantitativeCheckMasterDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentsException("Quantitative Check Master list cannot be empty");
        }
        quantitativeCheckMasterService.createAll(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Quantitative Check Masters created successfully", null, 0);
    }

    @Operation(summary = "Create a Quantitative Check Master")
    @PostMapping("/quantitativeCheckMaster")
    public ResponseEntity<Response<QuantitativeCheckMasterDTOResponse>> create(@Validated @RequestBody QuantitativeCheckMasterDTO dto) {
        QuantitativeCheckMasterDTOResponse response = quantitativeCheckMasterService.create(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Quantitative Check Master created successfully", response, 1);
    }

    @Operation(summary = "Get all Quantitative Check Masters with filters")
    @GetMapping("/quantitativeCheckMaster")
    public ResponseEntity<Response<List<QuantitativeCheckMasterDTOResponse>>> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) QuantitativeCheckMaster.CheckStatus checkStatus,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) List<Long> scanMasterIds, // ✅ foreign key filter
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {


        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<QuantitativeCheckMasterDTOResponse> result =
                quantitativeCheckMasterService.getAllWithFilters(name, description, checkStatus, status, scanMasterIds, pageable);

        return responseService.success(HttpStatus.OK.value(),
                "Fetched successfully",
                result.getContent(),
                result.getTotalElements());
    }

    @Operation(summary = "Get Quantitative Check Master by ID")
    @GetMapping("/quantitativeCheckMaster/{id}")
    public ResponseEntity<Response<QuantitativeCheckMasterDTOResponse>> getById(@PathVariable Long id) {
        QuantitativeCheckMasterDTOResponse response = quantitativeCheckMasterService.getById(id);
        return responseService.success(HttpStatus.OK.value(), "Fetched successfully", response, 1);
    }

    @Operation(summary = "Update Quantitative Check Master by ID")
    @PutMapping("/quantitativeCheckMaster/{id}")
    public ResponseEntity<Response<QuantitativeCheckMasterDTOResponse>> update(
            @PathVariable Long id,
            @Validated @RequestBody QuantitativeCheckMasterDTO dto) {
        QuantitativeCheckMasterDTOResponse response = quantitativeCheckMasterService.update(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Updated successfully", response, 1);
    }

    @Operation(summary = "Delete Quantitative Check Master by ID")
    @DeleteMapping("/quantitativeCheckMaster/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        quantitativeCheckMasterService.delete(id);
        return responseService.success(HttpStatus.OK.value(), "Deleted successfully", null, 0);
    }
}
