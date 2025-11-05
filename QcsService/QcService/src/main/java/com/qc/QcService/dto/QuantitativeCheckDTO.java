package com.qc.QcService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuantitativeCheckDTO {

    private String description;

    private Boolean isScan;

    private String status;

    @NotBlank(message = "Value is required")
    private String value;

    @NotNull(message = "QuantitativeCheckMaster ID is required")
    private Long quantitativeCheckMasterId;
}
