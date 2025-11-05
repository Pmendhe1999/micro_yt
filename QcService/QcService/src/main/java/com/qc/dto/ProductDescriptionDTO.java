package com.qc.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ProductDescriptionDTO {
    @NotNull(message = "Product ID is required")
    private Long productId;


    private String descriptions;

    public @NotNull(message = "Product ID is required") Long getProductId() {
        return productId;
    }

    public void setProductId(@NotNull(message = "Product ID is required") Long productId) {
        this.productId = productId;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}
