package com.quiz.controllers;

import com.quiz.dto.QualitativeCheckDTO;
import com.quiz.dto.QualitativeCheckDTOResponse;
import com.quiz.dto.Response;
import com.quiz.services.QualitativeCheckService;
import com.quiz.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
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

    @Operation(summary = "Get all Qualitative Checks")
    @GetMapping("/qualitativeCheck")
    public ResponseEntity<Response<List<QualitativeCheckDTOResponse>>> getAllQualitativeChecks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching all Qualitative Checks");
        Page<QualitativeCheckDTOResponse> resultPage = qualitativeCheckService.getAllQualitativeChecks(PageRequest.of(page - 1, size));
        return responseService.success(HttpStatus.OK.value(), "Qualitative Checks fetched successfully", resultPage.getContent(), resultPage.getTotalElements());
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

    @Operation(summary = "Patch Qualitative Check by ID")
    @PatchMapping("/qualitativeCheck/{id}")
    public ResponseEntity<Response<QualitativeCheckDTOResponse>> patchQualitativeCheck(
            @PathVariable Long id, @RequestBody QualitativeCheckDTO dto) {
        QualitativeCheckDTOResponse response = qualitativeCheckService.patchQualitativeCheck(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Qualitative Check patched successfully", response, 1);
    }

    @Operation(summary = "Delete Qualitative Check by ID")
    @DeleteMapping("/qualitativeCheck/{id}")
    public ResponseEntity<Response<Void>> deleteQualitativeCheck(@PathVariable Long id) {
        qualitativeCheckService.deleteQualitativeCheck(id);
        return responseService.success(HttpStatus.OK.value(), "Qualitative Check deleted successfully", null, 0);
    }
}
