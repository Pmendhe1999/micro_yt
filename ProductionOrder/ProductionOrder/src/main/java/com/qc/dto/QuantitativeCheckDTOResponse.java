package com.qc.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class QuantitativeCheckDTOResponse {
    private Long id;
    private String description;
    private Boolean isScan;
    private String status;
    private String value;
    private Long quantitativeCheckMasterId;
    private String quantitativeCheckMasterName; // optional if you want to show master name

    private Long productId;
    private String productName;

    public QuantitativeCheckDTOResponse(
            Long id,
            String description,
            Boolean scan,
            String status,
            String value,
            Long quantitativeCheckMasterId,
            String quantitativeCheckMasterName,
            Long productId,
            String productName,
            String productCode,
            String size,
            String orientation
    ) {
        this.id = id;
        this.description = description;
        this.isScan = scan;
        this.status = status;
        this.value = value;
        this.quantitativeCheckMasterId = quantitativeCheckMasterId;
        this.quantitativeCheckMasterName = quantitativeCheckMasterName;
        this.productId = productId;
        this.productName = productName;

    }

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

    public Long getQuantitativeCheckMasterId() {
        return quantitativeCheckMasterId;
    }

    public void setQuantitativeCheckMasterId(Long quantitativeCheckMasterId) {
        this.quantitativeCheckMasterId = quantitativeCheckMasterId;
    }

    public String getQuantitativeCheckMasterName() {
        return quantitativeCheckMasterName;
    }

    public void setQuantitativeCheckMasterName(String quantitativeCheckMasterName) {
        this.quantitativeCheckMasterName = quantitativeCheckMasterName;
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
