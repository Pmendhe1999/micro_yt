package com.qc.dto;

import com.qc.entities.LabelScanMaster;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LabelScanMasterDTOResponse {
    private Long id;
    private String name;
    private String description;
    private String scanType;
    private Long seqNumber;
    private LabelScanMaster.CheckStatus checkStatus;
    private Boolean status;

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
