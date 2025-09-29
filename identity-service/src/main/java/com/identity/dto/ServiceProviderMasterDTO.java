package com.identity.dto;

import jakarta.validation.constraints.NotBlank;

public class ServiceProviderMasterDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;

    private Boolean status;

    public  String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
