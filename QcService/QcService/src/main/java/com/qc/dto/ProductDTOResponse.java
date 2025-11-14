package com.qc.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTOResponse {
    private Long id;
    private String batchNo;
    private LocalDate expDate;
    private String hsnCode;
    private Long inStockQuantity;
    private Boolean isPublished;
    private LocalDate mfgDate;
    private String name;
    private String orderNo;
    private BigDecimal price;
    private String productCode;
    private String serialNo;
    private Boolean status;
    private String unit;

    private Long deliveryChallanId;
    private String deliveryChallanName;   // Optional for readability
    private Long deliveryItemId;
    private String deliveryItemName;
    private String orientation;
    private String size;

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getDeliveryChallanId() {
        return deliveryChallanId;
    }

    public void setDeliveryChallanId(Long deliveryChallanId) {
        this.deliveryChallanId = deliveryChallanId;
    }

    public String getDeliveryChallanName() {
        return deliveryChallanName;
    }

    public void setDeliveryChallanName(String deliveryChallanName) {
        this.deliveryChallanName = deliveryChallanName;
    }

    public Long getDeliveryItemId() {
        return deliveryItemId;
    }

    public void setDeliveryItemId(Long deliveryItemId) {
        this.deliveryItemId = deliveryItemId;
    }

    public String getDeliveryItemName() {
        return deliveryItemName;
    }

    public void setDeliveryItemName(String deliveryItemName) {
        this.deliveryItemName = deliveryItemName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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


}
