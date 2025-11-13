package com.qc.dto;

import java.util.List;

public class LabelScanQualitativeCheckRequestDTO {
    private Long labelScanMasterId;
    private Long deliveryChallanId;   // 🔹 NEW FIELD
    private List<QualitativeCheckRequestDTO> qualitativeChecks;

    public Long getDeliveryChallanId() {
        return deliveryChallanId;
    }

    public void setDeliveryChallanId(Long deliveryChallanId) {
        this.deliveryChallanId = deliveryChallanId;
    }

    public Long getLabelScanMasterId() {
        return labelScanMasterId;
    }

    public void setLabelScanMasterId(Long labelScanMasterId) {
        this.labelScanMasterId = labelScanMasterId;
    }

    public List<QualitativeCheckRequestDTO> getQualitativeChecks() {
        return qualitativeChecks;
    }

    public void setQualitativeChecks(List<QualitativeCheckRequestDTO> qualitativeChecks) {
        this.qualitativeChecks = qualitativeChecks;
    }
}
