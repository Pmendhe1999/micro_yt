package com.qc.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "production_order")
public class ProductionOrder extends AbstractAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "production_order_date")
    private LocalDate productionOrderDate;

    @Column(name = "production_order_no", length = 255)
    private String productionOrderNo;

    @Column(name = "batch_no", length = 255)
    private String batchNo;

    @Column(name = "order_quantity")
    private BigDecimal orderQuantity;

    @Column(name = "current_work_center", length = 255)
    private String currentWorkCenter;

    @Column(name = "activity_number", length = 100)
    private String activityNumber;

    @Column(name = "operation", length = 255)
    private String operation;

    @Column(name = "priority")
    private Long priority;

    @Column(name = "priority_remark", length = 500)
    private String priorityRemark;

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

    public BigDecimal  getOrderQuantity() {
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
