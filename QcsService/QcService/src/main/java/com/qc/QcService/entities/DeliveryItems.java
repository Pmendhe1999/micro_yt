package com.qc.QcService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryItems  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_no", length = 255)
    private String batchNo;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "exp_date")
    private LocalDate expDate;

    @Column(name = "hsn_code", length = 255)
    private String hsnCode;

    @Column(name = "mfg_date")
    private LocalDate mfgDate;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "order_no", length = 255)
    private String orderNo;

    @Column(name = "product_code", length = 255)
    private String productCode;

    @Column(name = "qualitative_check_failed_qty")
    private Long qualitativeCheckFailedQty;

    @Column(name = "qualitative_check_passed_qty")
    private Long qualitativeCheckPassedQty;

    @Column(name = "quantitative_check_failed_qty")
    private Long quantitativeCheckFailedQty;

    @Column(name = "quantitative_check_passed_qty")
    private Long quantitativeCheckPassedQty;

    @Column(name = "quantity", precision = 19, scale = 2)
    private BigDecimal quantity;

    @Column(name = "serial_no", length = 255)
    private String serialNo;

    @Column(name = "unit", length = 255)
    private String unit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challen_id")
    private DeliveryChallan deliveryChallan;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

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

    public DeliveryChallan getDeliveryChallan() {
        return deliveryChallan;
    }

    public void setDeliveryChallan(DeliveryChallan deliveryChallan) {
        this.deliveryChallan = deliveryChallan;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
