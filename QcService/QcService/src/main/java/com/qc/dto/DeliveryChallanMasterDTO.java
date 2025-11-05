package com.qc.dto;

import jakarta.validation.constraints.NotNull;

public class DeliveryChallanMasterDTO {


    @NotNull(message = "Name is required")
    private String name;

    private String descriptions;
    private Boolean status;

    public @NotNull(message = "Name is required") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Name is required") String name) {
        this.name = name;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
