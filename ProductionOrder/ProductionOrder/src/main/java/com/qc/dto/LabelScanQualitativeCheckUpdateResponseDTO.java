package com.qc.dto;

import lombok.Data;

import java.util.List;
@Data
public class LabelScanQualitativeCheckUpdateResponseDTO {
    private Long labelScanMasterId;
    private Long deliveryChallanId;
    private Long productId;
    private List<QualitativeCheckDTOResponse> qualitativeChecks;

    public LabelScanQualitativeCheckUpdateResponseDTO(Long labelScanMasterId, Long deliveryChallanId, Long productId, List<QualitativeCheckDTOResponse> qualitativeChecks) {
        this.labelScanMasterId = labelScanMasterId;
        this.deliveryChallanId = deliveryChallanId;
        this.productId = productId;
        this.qualitativeChecks = qualitativeChecks;
    }

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<QualitativeCheckDTOResponse> getQualitativeChecks() {
        return qualitativeChecks;
    }

    public void setQualitativeChecks(List<QualitativeCheckDTOResponse> qualitativeChecks) {
        this.qualitativeChecks = qualitativeChecks;
    }
}
