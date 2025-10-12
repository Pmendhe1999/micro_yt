package com.qc.QcService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuantitativeCheckMasterDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Check status is required")
    private String checkStatus;

    private String status;

    @NotNull(message = "Scan Master ID is required")
    private Long scanMasterId;

    public  String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public  String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus( String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getScanMasterId() {
        return scanMasterId;
    }

    public void setScanMasterId( Long scanMasterId) {
        this.scanMasterId = scanMasterId;
    }
}
