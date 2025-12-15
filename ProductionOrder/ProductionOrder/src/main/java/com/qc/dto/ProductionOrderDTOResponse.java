package com.qc.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
public class ProductionOrderDTOResponse {
    private Long id;
    private LocalDate productionOrderDate;
    private String productionOrderNo;
    private String batchNo;
    private Double orderQuantity;
    private String currentWorkCenter;
    private String activityNumber;
    private String operation;
    private Long priority;
    private String priorityRemark;

    private Instant createdDate;          // ADD THIS
    private Instant lastModifiedDate;     // ADD THIS
    private String productCode;
    private String productDescription;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getProductionOrderDate() {
        return productionOrderDate;
    }

    public void setProductionOrderDate(LocalDate productionOrderDate) {
        this.productionOrderDate = productionOrderDate;
    }

    public String getProductionOrderNo() {
        return productionOrderNo;
    }

    public void setProductionOrderNo(String productionOrderNo) {
        this.productionOrderNo = productionOrderNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Double getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Double orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getCurrentWorkCenter() {
        return currentWorkCenter;
    }

    public void setCurrentWorkCenter(String currentWorkCenter) {
        this.currentWorkCenter = currentWorkCenter;
    }

    public String getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(String activityNumber) {
        this.activityNumber = activityNumber;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getPriorityRemark() {
        return priorityRemark;
    }

    public void setPriorityRemark(String priorityRemark) {
        this.priorityRemark = priorityRemark;
    }
}
