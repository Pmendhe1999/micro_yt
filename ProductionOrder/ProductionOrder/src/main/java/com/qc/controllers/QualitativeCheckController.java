package com.qc.controllers;

import com.qc.dto.QualitativeCheckDTO;
import com.qc.dto.QualitativeCheckDTOResponse;
import com.qc.dto.Response;
import com.qc.services.QualitativeCheckService;
import com.qc.services.ResponseService;
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
@RequestMapping("/qc")
@Slf4j
public class QualitativeCheckController {

    @Autowired
    private ResponseService responseService;

    @Autowired
    private QualitativeCheckService qualitativeCheckService;

    @Operation(summary = "Create multiple Qualitative Checks")
    @PostMapping("/qualitativeCheck/all")
    public ResponseEntity<Response<Void>> createAllQualitativeChecks(@Valid @RequestBody List<QualitativeCheckDTO> dtoList) {
        log.info("Request to create list of Qualitative Checks");
        qualitativeCheckService.createAllQualitativeCheck(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Qualitative Checks created successfully", null, 0);
    }

    @Operation(summary = "Create a single Qualitative Check")
    @PostMapping("/qualitativeCheck")
    public ResponseEntity<Response<QualitativeCheckDTOResponse>> createQualitativeCheck(@Valid @RequestBody QualitativeCheckDTO dto) {
        log.info("Request to create Qualitative Check");
        QualitativeCheckDTOResponse response = qualitativeCheckService.createQualitativeCheck(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Qualitative Check created successfully", response, 1);
    }

    @Operation(summary = "Get all Qualitative Checks with filters")
    @GetMapping("/qualitativeCheck")
    public ResponseEntity<Response<List<QualitativeCheckDTOResponse>>> getAllQualitativeChecks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean isScan,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String value,
            @RequestParam(required = false) List<Long> qualitativeCheckMasterIds,
            @RequestParam(required = false) List<Long> productIds, // ✅ added product filter
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<QualitativeCheckDTOResponse> resultPage =
                qualitativeCheckService.getAllQualitativeChecksWithFilters(
                        description, isScan, status, value, qualitativeCheckMasterIds, productIds, pageable);

        return responseService.success(HttpStatus.OK.value(),
                "Qualitative Checks fetched successfully",
                resultPage.getContent(),
                resultPage.getTotalElements());
    }

    @Operation(summary = "Get Qualitative Check by ID")
    @GetMapping("/qualitativeCheck/{id}")
    public ResponseEntity<Response<QualitativeCheckDTOResponse>> getQualitativeCheckById(@PathVariable Long id) {
        QualitativeCheckDTOResponse response = qualitativeCheckService.getQualitativeCheckById(id);
        return responseService.success(HttpStatus.OK.value(), "Qualitative Check fetched successfully", response, 1);
    }

    @Operation(summary = "Update Qualitative Check by ID")
    @PutMapping("/qualitativeCheck/{id}")
    public ResponseEntity<Response<QualitativeCheckDTOResponse>> updateQualitativeCheck(
            @PathVariable Long id, @Valid @RequestBody QualitativeCheckDTO dto) {
        QualitativeCheckDTOResponse response = qualitativeCheckService.updateQualitativeCheck(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Qualitative Check updated successfully", response, 1);
    }

//    @Operation(summary = "Patch Qualitative Check by ID")
//    @PatchMapping("/qualitativeCheck/{id}")
//    public ResponseEntity<Response<QualitativeCheckDTOResponse>> patchQualitativeCheck(
//            @PathVariable Long id, @RequestBody QualitativeCheckDTO dto) {
//        QualitativeCheckDTOResponse response = qualitativeCheckService.patchQualitativeCheck(id, dto);
//        return responseService.success(HttpStatus.OK.value(), "Qualitative Check patched successfully", response, 1);
//    }

    @Operation(summary = "Delete Qualitative Check by ID")
    @DeleteMapping("/qualitativeCheck/{id}")
    public ResponseEntity<Response<Void>> deleteQualitativeCheck(@PathVariable Long id) {
        qualitativeCheckService.deleteQualitativeCheck(id);
        return responseService.success(HttpStatus.OK.value(), "Qualitative Check deleted successfully", null, 0);
    }
}
