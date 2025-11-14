package com.qc.dto;

import java.util.List;

public class LabelScanQuantitativeCheckRequestDTO {
    private Long labelScanMasterId;
    private Long deliveryChallanId;

    private List<QuantitativeCheckRequestDTO> quantitativeChecks;

    public Long getLabelScanMasterId() {
        return labelScanMasterId;
    }
    public void setLabelScanMasterId(Long labelScanMasterId) {
        this.labelScanMasterId = labelScanMasterId;
    }

    public Long getDeliveryChallanId() {
        return deliveryChallanId;
    }
    public void setDeliveryChallanId(Long deliveryChallanId) {
        this.deliveryChallanId = deliveryChallanId;
    }

    public List<QuantitativeCheckRequestDTO> getQuantitativeChecks() {
        return quantitativeChecks;
    }
    public void setQuantitativeChecks(List<QuantitativeCheckRequestDTO> quantitativeChecks) {
        this.quantitativeChecks = quantitativeChecks;
    }
}
