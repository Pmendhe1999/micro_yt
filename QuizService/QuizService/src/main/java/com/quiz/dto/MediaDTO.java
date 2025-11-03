package com.quiz.dto;

import jakarta.validation.constraints.NotBlank;

public class MediaDTO {
    @NotBlank(message = "Base Image URL is required")
    private String baseImageUrl;

    private String description;
    private String name;
    private String status;
    private String type;

    public @NotBlank(message = "Base Image URL is required") String getBaseImageUrl() {
        return baseImageUrl;
    }

    public void setBaseImageUrl(@NotBlank(message = "Base Image URL is required") String baseImageUrl) {
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
