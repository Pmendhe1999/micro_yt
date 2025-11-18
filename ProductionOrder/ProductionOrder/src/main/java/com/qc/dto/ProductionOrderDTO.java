package com.qc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductionOrderDTO {
    private LocalDate productionOrderDate;
    private String productionOrderNo;
    private String batchNo;
    private BigDecimal orderQuantity;
    private String currentWorkCenter;
    private String activityNumber;
    private String operation;
    private Long priority;
    private String priorityRemark;

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

    public BigDecimal getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(BigDecimal orderQuantity) {
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
