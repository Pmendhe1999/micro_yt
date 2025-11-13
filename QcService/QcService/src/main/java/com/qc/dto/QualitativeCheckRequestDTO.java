package com.qc.dto;

public class QualitativeCheckRequestDTO {
    private Long qualitativeCheckMasterId;
    private String productName;
    private String description;
    private Boolean isScan;
    private String status;
    private String value;

    public Long getQualitativeCheckMasterId() {
        return qualitativeCheckMasterId;
    }

    public void setQualitativeCheckMasterId(Long qualitativeCheckMasterId) {
        this.qualitativeCheckMasterId = qualitativeCheckMasterId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

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

    public void setValue(String value) {
        this.value = value;
    }
}
