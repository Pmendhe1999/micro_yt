package com.qc.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "qualitative_check")
public class QualitativeCheck extends AbstractAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_scan")
    private Boolean isScan;

    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "value", length = 255)
    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "qualitative_check_master_id", nullable = false)
    private QualitativeCheckMaster qualitativeCheckMaster;

    // ✅ New foreign key relation with Product
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public QualitativeCheckMaster getQualitativeCheckMaster() {
        return qualitativeCheckMaster;
    }

    public void setQualitativeCheckMaster(QualitativeCheckMaster qualitativeCheckMaster) {
        this.qualitativeCheckMaster = qualitativeCheckMaster;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
