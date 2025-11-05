package com.qc.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name = "delivery_items_master")
public class DeliveryItemsMaster extends AbstractAuditingEntity{

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

    @Column(name = "quantity", precision = 19, scale = 2)
    private BigDecimal quantity;

    @Column(name = "serial_no", length = 255)
    private String serialNo;

    @Column(name = "unit", length = 255)
    private String unit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challen_id", nullable = false)
    private DeliveryChallanMaster challan;

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

    public DeliveryChallanMaster getChallan() {
        return challan;
    }

    public void setChallan(DeliveryChallanMaster challan) {
        this.challan = challan;
    }
}
