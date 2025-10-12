package com.qc.QcService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualitativeCheckDTO {
    private String description;

    private Boolean isScan;

    private String status;

    @NotBlank(message = "Value is required")
    private String value;

    @NotNull(message = "QualitativeCheckMaster ID is required")
    private Long qualitativeCheckMasterId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getScan() {
        return isScan;
    }

    public void setScan(Boolean scan) {
        isScan = scan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue( String value) {
        this.value = value;
    }

    public  Long getQualitativeCheckMasterId() {
        return qualitativeCheckMasterId;
    }

    public void setQualitativeCheckMasterId(Long qualitativeCheckMasterId) {
        this.qualitativeCheckMasterId = qualitativeCheckMasterId;
    }
}
