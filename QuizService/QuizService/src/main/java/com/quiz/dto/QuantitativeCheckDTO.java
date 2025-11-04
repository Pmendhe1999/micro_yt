package com.quiz.dto;

public class QuantitativeCheckDTO {
    private String description;
    private Boolean isScan;
    private String status;
    private String value;
    private Long quantitativeCheckMasterId;
    private Long productId;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
