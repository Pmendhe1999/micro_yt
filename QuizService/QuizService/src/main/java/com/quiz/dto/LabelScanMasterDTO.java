package com.quiz.dto;

import com.quiz.entities.LabelScanMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class LabelScanMasterDTO {
    private Long id;
    private String name;
    private String description;
    private String scanType;
    private Long seqNumber;
    private LabelScanMaster.CheckStatus checkStatus;
    private String status;

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

    public LabelScanMaster.CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(LabelScanMaster.CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
