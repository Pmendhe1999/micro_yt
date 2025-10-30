package com.quiz.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductDTO {
    private String batchNo;
    private LocalDate expDate;
    private String hsnCode;
    private Long inStockQuantity;
    private Boolean isPublished;
    private LocalDate mfgDate;

    @NotNull(message = "Product name is required")
    private String name;

    private String orderNo;
    private BigDecimal price;
    private String productCode;
    private String serialNo;
    private Boolean status;
    private String unit;

    private Long qualitativeCheckId;
    private Long quantitativeCheckId;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public Long getInStockQuantity() {
        return inStockQuantity;
    }

    public void setInStockQuantity(Long inStockQuantity) {
        this.inStockQuantity = inStockQuantity;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public LocalDate getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(LocalDate mfgDate) {
        this.mfgDate = mfgDate;
    }

    public @NotNull(message = "Product name is required") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Product name is required") String name) {
        this.name = name;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getQualitativeCheckId() {
        return qualitativeCheckId;
    }

    public void setQualitativeCheckId(Long qualitativeCheckId) {
        this.qualitativeCheckId = qualitativeCheckId;
    }

    public Long getQuantitativeCheckId() {
        return quantitativeCheckId;
    }

    public void setQuantitativeCheckId(Long quantitativeCheckId) {
        this.quantitativeCheckId = quantitativeCheckId;
    }
}
