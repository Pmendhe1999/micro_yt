package com.quiz.controllers;

import com.quiz.dto.LabelScanMasterDTO;
import com.quiz.dto.LabelScanMasterDTOResponse;
import com.quiz.dto.Response;
import com.quiz.exception.IllegalArgumentsException;
import com.quiz.services.LabelScanMasterService;
import com.quiz.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class LabelScanMasterController {
    private final ResponseService responseService;
    private final LabelScanMasterService labelScanMasterService;

    public LabelScanMasterController(ResponseService responseService, LabelScanMasterService labelScanMasterService) {
        this.responseService = responseService;
        this.labelScanMasterService = labelScanMasterService;
    }

    @Operation(summary = "Create a List of Label Scan Masters")
    @PostMapping("/labelScanMaster/all")
    public ResponseEntity<Response<Void>> createAllLabelScanMaster(@Valid @RequestBody List<LabelScanMasterDTO> dtoList) {
        log.info("Request to create list of LabelScanMaster");
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentsException("Label Scan Master list cannot be empty");
        }
        labelScanMasterService.createAllLabelScanMaster(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Label Scan Masters created successfully", null, 0);
    }

    @Operation(summary = "Create a Label Scan Master")
    @PostMapping("/labelScanMaster")
    public ResponseEntity<Response<LabelScanMasterDTOResponse>> createLabelScanMaster(
            @Valid @RequestBody LabelScanMasterDTO dto) {
        log.info("Request to create LabelScanMaster: {}", dto.getName());
        labelScanMasterService.createLabelScanMaster(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Label Scan Master created successfully", null, 0);
    }

    @Operation(summary = "Update Label Scan Master by ID")
    @PutMapping("/labelScanMaster/{id}")
    public ResponseEntity<Response<LabelScanMasterDTOResponse>> updateLabelScanMaster(
            @PathVariable Long id, @Valid @RequestBody LabelScanMasterDTO dto) {
        log.info("Request to update LabelScanMaster with ID: {}", id);
        LabelScanMasterDTOResponse updated = labelScanMasterService.updateLabelScanMaster(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Label Scan Master updated successfully", updated, 1);
    }

    @Operation(summary = "Partially update Label Scan Master by ID")
    @PatchMapping("/labelScanMaster/{id}")
    public ResponseEntity<Response<LabelScanMasterDTOResponse>> patchLabelScanMaster(
            @PathVariable Long id, @RequestBody LabelScanMasterDTO dto) {
        log.info("Request to patch LabelScanMaster with ID: {}", id);
        LabelScanMasterDTOResponse updated = labelScanMasterService.patchLabelScanMaster(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Label Scan Master partially updated successfully", updated, 1);
    }

    @Operation(summary = "List all Label Scan Masters with pagination")
    @GetMapping("/labelScanMaster")
    public ResponseEntity<Response<List<LabelScanMasterDTOResponse>>> getAllLabelScanMaster(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Request to fetch LabelScanMasters, page: {}, size: {}", page, size);
        Page<LabelScanMasterDTOResponse> result = labelScanMasterService.getAllLabelScanMaster(PageRequest.of(page, size));
        return responseService.success(HttpStatus.OK.value(), "Label Scan Masters fetched successfully",
                result.getContent(), result.getTotalElements());
    }

    @Operation(summary = "Get Label Scan Master by ID")
    @GetMapping("/labelScanMaster/{id}")
    public ResponseEntity<Response<LabelScanMasterDTOResponse>> getLabelScanMasterById(@PathVariable Long id) {
        log.info("Request to get LabelScanMaster with ID: {}", id);
        LabelScanMasterDTOResponse result = labelScanMasterService.getLabelScanMasterById(id);
        return responseService.success(HttpStatus.OK.value(), "Label Scan Master fetched successfully", result, 1);
    }

    @Operation(summary = "Delete Label Scan Master by ID")
    @DeleteMapping("/labelScanMaster/{id}")
    public ResponseEntity<Response<Void>> deleteLabelScanMaster(@PathVariable Long id) {
        log.info("Request to delete LabelScanMaster with ID: {}", id);
        labelScanMasterService.deleteLabelScanMaster(id);
        return responseService.success(HttpStatus.OK.value(), "Label Scan Master deleted successfully", null, 0);
    }
}
