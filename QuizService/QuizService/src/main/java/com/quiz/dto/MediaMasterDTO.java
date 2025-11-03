package com.quiz.dto;

import jakarta.validation.constraints.NotNull;

public class MediaMasterDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    private String baseImageUrl;
    private String description;
    private String name;
    private String status;
    private String type;

    public @NotNull(message = "Product ID is required") Long getProductId() {
        return productId;
    }

    public void setProductId(@NotNull(message = "Product ID is required") Long productId) {
        this.productId = productId;
    }

    public String getBaseImageUrl() {
        return baseImageUrl;
    }

    public void setBaseImageUrl(String baseImageUrl) {
        this.baseImageUrl = baseImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
