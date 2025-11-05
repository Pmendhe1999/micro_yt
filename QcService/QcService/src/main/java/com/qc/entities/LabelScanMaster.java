package com.qc.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "label_scan_master")
public class LabelScanMaster extends AbstractAuditingEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "scan_type", length = 255)
    private String scanType;

    @Column(name = "seq_number")
    private Long seqNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_status", length = 50)
    private CheckStatus checkStatus;

    @Column(name = "status", length = 255)
    private String status;

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

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public Long getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Long seqNumber) {
        this.seqNumber = seqNumber;
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
}
