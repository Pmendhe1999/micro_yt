package com.quiz.controllers;

import com.quiz.dto.QualitativeCheckMasterDTO;
import com.quiz.dto.QualitativeCheckMasterDTOResponse;
import com.quiz.dto.Response;
import com.quiz.entities.QualitativeCheckMaster;
import com.quiz.exception.IllegalArgumentsException;
import com.quiz.services.QualitativeCheckMasterService;
import com.quiz.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
@Slf4j
public class QualitativeCheckMasterController {

    @Autowired
    private ResponseService responseService;

    @Autowired
    private QualitativeCheckMasterService service;

    @Operation(summary = "Create a List of Qualitative Check Masters")
    @PostMapping("/qualitativeCheckMaster/all")
    public ResponseEntity<Response<Void>> createAll(@Valid @RequestBody List<QualitativeCheckMasterDTO> dtoList) {
        log.info("Request to create multiple QualitativeCheckMasters");
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentsException("Qualitative Check Master list cannot be empty");
        }
        service.createAllQualitativeCheckMaster(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Qualitative Check Masters created successfully", null, 0);
    }

    @Operation(summary = "Create a Qualitative Check Master")
    @PostMapping("/qualitativeCheckMaster")
    public ResponseEntity<Response<QualitativeCheckMasterDTOResponse>> create(@Valid @RequestBody QualitativeCheckMasterDTO dto) {
        log.info("Request to create Qualitative Check Master: {}", dto.getName());
        service.createQualitativeCheckMaster(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Qualitative Check Master created successfully", null, 0);
    }

    @Operation(summary = "Update Qualitative Check Master by ID")
    @PutMapping("/qualitativeCheckMaster/{id}")
    public ResponseEntity<Response<QualitativeCheckMasterDTOResponse>> update(@PathVariable Long id, @Valid @RequestBody QualitativeCheckMasterDTO dto) {
        QualitativeCheckMasterDTOResponse updated = service.updateQualitativeCheckMaster(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Qualitative Check Master updated successfully", updated, 1);
    }

    @Operation(summary = "Get all Qualitative Check Masters with filters and pagination")
    @GetMapping("/qualitativeCheckMaster")
    public ResponseEntity<Response<List<QualitativeCheckMasterDTOResponse>>> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) QualitativeCheckMaster.CheckStatus checkStatus,
            @RequestParam(required = false) List<Long> scanMasterIds, // ✅ foreign key filter (LabelScanMaster)
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {



        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<QualitativeCheckMasterDTOResponse> resultPage = service.getAllQualitativeCheckMastersWithFilters(
                name, description, status, checkStatus, scanMasterIds, pageable);

        return responseService.success(HttpStatus.OK.value(),
                "Qualitative Check Masters fetched successfully",
                resultPage.getContent(),
                resultPage.getTotalElements());
    }
    @Operation(summary = "Get Qualitative Check Master by ID")
    @GetMapping("/qualitativeCheckMaster/{id}")
    public ResponseEntity<Response<QualitativeCheckMasterDTOResponse>> getById(@PathVariable Long id) {
        QualitativeCheckMasterDTOResponse result = service.getQualitativeCheckMasterById(id);
        return responseService.success(HttpStatus.OK.value(), "Qualitative Check Master fetched successfully", result, 1);
    }

    @Operation(summary = "Delete Qualitative Check Master by ID")
    @DeleteMapping("/qualitativeCheckMaster/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        service.deleteQualitativeCheckMaster(id);
        return responseService.success(HttpStatus.OK.value(), "Qualitative Check Master deleted successfully", null, 0);
    }
}
