package com.quiz.dto;

import com.quiz.entities.QualitativeCheckMaster;


public class QualitativeCheckMasterDTO {
    private String name;
    private String description;
    private QualitativeCheckMaster.CheckStatus checkStatus;
    private String status;
    private Long scanMasterId;

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

    public QualitativeCheckMaster.CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(QualitativeCheckMaster.CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getScanMasterId() {
        return scanMasterId;
    }

    public void setScanMasterId(Long scanMasterId) {
        this.scanMasterId = scanMasterId;
    }
}
