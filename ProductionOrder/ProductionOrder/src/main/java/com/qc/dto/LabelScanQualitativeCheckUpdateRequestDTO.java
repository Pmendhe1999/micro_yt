package com.qc.dto;


import java.util.List;

public class LabelScanQualitativeCheckUpdateRequestDTO {
    private Long deliveryChallanId;
    private Long labelScanMasterId;
    private Long productId;
    private Long mediaId; // NEW
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public List<QualitativeCheckRequestDTO> getQualitativeChecks() {
        return qualitativeChecks;
    }

    public void setQualitativeChecks(List<QualitativeCheckRequestDTO> qualitativeChecks) {
        this.qualitativeChecks = qualitativeChecks;
    }
}
