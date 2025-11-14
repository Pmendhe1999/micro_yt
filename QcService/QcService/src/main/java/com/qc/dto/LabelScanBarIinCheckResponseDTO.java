package com.qc.dto;

import java.util.List;

public class LabelScanBarIinCheckResponseDTO {
    private Long labelScanMasterId;
    private Long deliveryChallanId;
    private Long productId;

    private List<QuantitativeCheckDTOResponse> quantitativeChecks;

    public LabelScanBarIinCheckResponseDTO(Long labelScanMasterId,
                                           Long deliveryChallanId,
                                           Long productId,
                                           List<QuantitativeCheckDTOResponse> quantitativeChecks) {
        this.labelScanMasterId = labelScanMasterId;
        this.deliveryChallanId = deliveryChallanId;
        this.productId = productId;
        this.quantitativeChecks = quantitativeChecks;
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

    public List<QuantitativeCheckDTOResponse> getQuantitativeChecks() {
        return quantitativeChecks;
    }

    public void setQuantitativeChecks(List<QuantitativeCheckDTOResponse> quantitativeChecks) {
        this.quantitativeChecks = quantitativeChecks;
    }
}
