package com.qc.QcService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDescriptionDTO {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Description is required")
    private String descriptions;

    public @NotNull(message = "Product ID is required") Long getProductId() {
        return productId;
    }

    public void setProductId(@NotNull(message = "Product ID is required") Long productId) {
        this.productId = productId;
    }

    public @NotBlank(message = "Description is required") String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(@NotBlank(message = "Description is required") String descriptions) {
        this.descriptions = descriptions;
    }
}
