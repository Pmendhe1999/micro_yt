package com.qc.QcService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "quantitative_check")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuantitativeCheck {

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
    @JoinColumn(name = "quantitative_check_master_id", nullable = false)
    private QuantitativeCheckMaster quantitativeCheckMaster;

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

    public QuantitativeCheckMaster getQuantitativeCheckMaster() {
        return quantitativeCheckMaster;
    }

    public void setQuantitativeCheckMaster(QuantitativeCheckMaster quantitativeCheckMaster) {
        this.quantitativeCheckMaster = quantitativeCheckMaster;
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
