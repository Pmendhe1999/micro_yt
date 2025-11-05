package com.qc.QcService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMasterDescriptionDTO {

    private Long productMasterId;
    private String descriptions;

    public Long getProductMasterId() {
        return productMasterId;
    }

    public void setProductMasterId(Long productMasterId) {
        this.productMasterId = productMasterId;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}
