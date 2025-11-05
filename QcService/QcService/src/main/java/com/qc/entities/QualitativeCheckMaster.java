package com.qc.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "qualitative_check_master")
public class QualitativeCheckMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_status", length = 50)
    private CheckStatus checkStatus;

    @Column(name = "status", length = 255)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scan_master_id", nullable = false)
    private LabelScanMaster scanMaster;

    public enum CheckStatus {
        QUALITATIVE,
        QUANTITATIVE
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LabelScanMaster getScanMaster() {
        return scanMaster;
    }

    public void setScanMaster(LabelScanMaster scanMaster) {
        this.scanMaster = scanMaster;
    }
}
