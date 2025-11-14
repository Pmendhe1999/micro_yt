package com.qc.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "product")
public class Product extends AbstractAuditingEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_no", length = 255)
    private String batchNo;

    @Column(name = "exp_date")
    private LocalDate expDate;

    @Column(name = "hsn_code", length = 255)
    private String hsnCode;

    @Column(name = "in_stock_quantity")
    private Long inStockQuantity;

    @Column(name = "is_published")
    private Boolean isPublished;

    @Column(name = "mfg_date")
    private LocalDate mfgDate;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "order_no", length = 255)
    private String orderNo;

    @Column(name = "price", precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "product_code", length = 255)
    private String productCode;

    @Column(name = "serial_no", length = 255)
    private String serialNo;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "unit", length = 255)
    private String unit;

    @Column(name = "size", length = 255)
    private String size;

    @Column(name = "orientation", length = 255)
    private String orientation;
    // ✅ Newly added field
    @Column(name = "mfg_lif_no", length = 255)
    private String mfgLifNo;

    // ✅ Newly added field
    @Column(name = "sterile_type", length = 255)
    private String sterileType;
    // Foreign key to DeliveryChallan
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_challan_id", nullable = false)
    private DeliveryChallan deliveryChallan;

    // Foreign key to DeliveryItemsMaster
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_item_id", nullable = true)
    private DeliveryItemsMaster deliveryItem;

    // Foreign key to DeliveryItemsMaster
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_items_id", nullable = false)
    private DeliveryItems deliveryItems;

    // 🔹 Foreign key to ProductMaster
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_master_id", nullable = false)
    private ProductMaster productMaster;

    // ✅ Newly Added Fields
    @Column(name = "bar_code_no", length = 255)
    private String barCodeNo;

    @Column(name = "iin_no", length = 255)
    private String iinNo;

    public String getBarCodeNo() {
        return barCodeNo;
    }

    public void setBarCodeNo(String barCodeNo) {
        this.barCodeNo = barCodeNo;
    }

    public String getIinNo() {
        return iinNo;
    }

    public void setIinNo(String iinNo) {
        this.iinNo = iinNo;
    }

    public String getMfgLifNo() {
        return mfgLifNo;
    }

    public void setMfgLifNo(String mfgLifNo) {
        this.mfgLifNo = mfgLifNo;
    }

    public String getSterileType() {
        return sterileType;
    }

    public void setSterileType(String sterileType) {
        this.sterileType = sterileType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public DeliveryItems getDeliveryItems() {
        return deliveryItems;
    }

    public void setDeliveryItems(DeliveryItems deliveryItems) {
        this.deliveryItems = deliveryItems;
    }

    public ProductMaster getProductMaster() {
        return productMaster;
    }

    public void setProductMaster(ProductMaster productMaster) {
        this.productMaster = productMaster;
    }

    public DeliveryItemsMaster getDeliveryItem() {
        return deliveryItem;
    }

    public void setDeliveryItem(DeliveryItemsMaster deliveryItem) {
        this.deliveryItem = deliveryItem;
    }

    public DeliveryChallan getDeliveryChallan() {
        return deliveryChallan;
    }

    public void setDeliveryChallan(DeliveryChallan deliveryChallan) {
        this.deliveryChallan = deliveryChallan;
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
