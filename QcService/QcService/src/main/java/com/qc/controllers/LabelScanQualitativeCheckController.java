package com.qc.controllers;

import com.qc.dto.LabelScanQualitativeCheckRequestDTO;
import com.qc.dto.LabelScanQuantitativeCheckRequestDTO;
import com.qc.dto.LabelScanQuantitativeCheckResponseDTO;
import com.qc.dto.Response;
import com.qc.services.LabelScanQualitativeCheckService;
import com.qc.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qc")
@Slf4j
public class LabelScanQualitativeCheckController {

    private final ResponseService responseService;
    private final LabelScanQualitativeCheckService labelScanQualitativeCheckService;

    public LabelScanQualitativeCheckController(ResponseService responseService,
                                               LabelScanQualitativeCheckService labelScanQualitativeCheckService) {
        this.responseService = responseService;
        this.labelScanQualitativeCheckService = labelScanQualitativeCheckService;
    }

    @Operation(summary = "Create Qualitative Checks for Label Scan Master")
    @PostMapping("/labelScanMaster/qualitativeCheck")
    public ResponseEntity<Response<Void>> createQualitativeChecks(
            @Valid @RequestBody LabelScanQualitativeCheckRequestDTO request) {
        log.info("Creating Qualitative Checks for LabelScanMaster ID: {}", request.getLabelScanMasterId());
        labelScanQualitativeCheckService.createQualitativeChecks(request);
        return responseService.success(HttpStatus.CREATED.value(),
                "Qualitative Checks created successfully", null, 0);
    }

    @PostMapping("/labelScanMaster/quantitativeCheck")
    public ResponseEntity<Response<LabelScanQuantitativeCheckResponseDTO>> createQuantitativeChecks(
            @Valid @RequestBody LabelScanQuantitativeCheckRequestDTO request) {

        LabelScanQuantitativeCheckResponseDTO dto =
                labelScanQualitativeCheckService.createQuantitativeChecks(request);

        return responseService.success(
                HttpStatus.CREATED.value(),
                "Quantitative Checks created successfully",
                dto,
                dto.getQuantitativeChecks().size()
        );
    }

}
