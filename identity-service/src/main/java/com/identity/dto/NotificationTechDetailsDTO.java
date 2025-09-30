package com.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NotificationTechDetailsDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String notes;

    private Boolean status;

    private Long serviceProviderMasterId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getServiceProviderMasterId() {
        return serviceProviderMasterId;
    }

    public void setServiceProviderMasterId(Long serviceProviderMasterId) {
        this.serviceProviderMasterId = serviceProviderMasterId;
    }
}
