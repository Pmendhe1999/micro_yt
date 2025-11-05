package com.qc.QcService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryItemsDTO {


    private String batchNo;
    private String description;
    private LocalDate expDate;
    private String hsnCode;
    private LocalDate mfgDate;
    private String name;
    private String orderNo;
    private String productCode;
    private Long qualitativeCheckFailedQty;
    private Long qualitativeCheckPassedQty;
    private Long quantitativeCheckFailedQty;
    private Long quantitativeCheckPassedQty;
    private BigDecimal quantity;
    private String serialNo;
    private String unit;

    @NotNull(message = "DeliveryChallan ID is required")
    private Long challenId;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public LocalDate getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(LocalDate mfgDate) {
        this.mfgDate = mfgDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getQualitativeCheckFailedQty() {
        return qualitativeCheckFailedQty;
    }

    public void setQualitativeCheckFailedQty(Long qualitativeCheckFailedQty) {
        this.qualitativeCheckFailedQty = qualitativeCheckFailedQty;
    }

    public Long getQualitativeCheckPassedQty() {
        return qualitativeCheckPassedQty;
    }

    public void setQualitativeCheckPassedQty(Long qualitativeCheckPassedQty) {
        this.qualitativeCheckPassedQty = qualitativeCheckPassedQty;
    }

    public Long getQuantitativeCheckFailedQty() {
        return quantitativeCheckFailedQty;
    }

    public void setQuantitativeCheckFailedQty(Long quantitativeCheckFailedQty) {
        this.quantitativeCheckFailedQty = quantitativeCheckFailedQty;
    }

    public Long getQuantitativeCheckPassedQty() {
        return quantitativeCheckPassedQty;
    }

    public void setQuantitativeCheckPassedQty(Long quantitativeCheckPassedQty) {
        this.quantitativeCheckPassedQty = quantitativeCheckPassedQty;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public  Long getChallenId() {
        return challenId;
    }

    public void setChallenId(Long challenId) {
        this.challenId = challenId;
    }
}
