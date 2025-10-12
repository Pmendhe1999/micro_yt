package com.qc.QcService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualitativeCheckMasterDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Check status is required")
    private String checkStatus; // "GOODTOHAVE" or "MUSTHAVE"

    private String status;

    @NotNull(message = "Scan Master ID is required")
    private Long scanMasterId;
}
