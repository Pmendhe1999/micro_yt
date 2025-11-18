package com.qc.dto;

import java.util.List;

public class LabelScanQuantitativeCheckResponseDTO {
    private Long productId;
    private Long labelScanMasterId;
    private Long deliveryChallanId;

    private List<QuantitativeCheckDTOResponse> quantitativeChecks;
    private List<MediaDetailsDTO> mediaDetails;   // ★ NEW

    public LabelScanQuantitativeCheckResponseDTO(Long productId, Long labelScanMasterId, Long deliveryChallanId, List<QuantitativeCheckDTOResponse> quantitativeChecks, List<MediaDetailsDTO> mediaDetails) {
        this.productId = productId;
        this.labelScanMasterId = labelScanMasterId;
        this.deliveryChallanId = deliveryChallanId;
        this.quantitativeChecks = quantitativeChecks;
        this.mediaDetails = mediaDetails;
    }

    public List<MediaDetailsDTO> getMediaDetails() {
        return mediaDetails;
    }

    public void setMediaDetails(List<MediaDetailsDTO> mediaDetails) {
        this.mediaDetails = mediaDetails;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public List<QuantitativeCheckDTOResponse> getQuantitativeChecks() {
        return quantitativeChecks;
    }

    public void setQuantitativeChecks(List<QuantitativeCheckDTOResponse> quantitativeChecks) {
        this.quantitativeChecks = quantitativeChecks;
    }
}
