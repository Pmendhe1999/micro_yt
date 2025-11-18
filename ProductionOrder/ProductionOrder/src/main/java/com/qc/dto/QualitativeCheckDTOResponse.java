package com.qc.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class QualitativeCheckDTOResponse {
    private Long id;
    private String description;
    private Boolean isScan;
    private String status;
    private String value;
    private Long qualitativeCheckMasterId;
    private String qualitativeCheckMasterName; // optional for readability
    private Long productId;           // ✅ added product info
    private String productName;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getQualitativeCheckMasterId() {
        return qualitativeCheckMasterId;
    }

    public void setQualitativeCheckMasterId(Long qualitativeCheckMasterId) {
        this.qualitativeCheckMasterId = qualitativeCheckMasterId;
    }

    public String getQualitativeCheckMasterName() {
        return qualitativeCheckMasterName;
    }

    public void setQualitativeCheckMasterName(String qualitativeCheckMasterName) {
        this.qualitativeCheckMasterName = qualitativeCheckMasterName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
