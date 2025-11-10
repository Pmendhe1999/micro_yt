package com.qc.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class DeliveryChallanMasterDTO {


    private String name;
    private String descriptions;
    private Boolean status;

    private List<DeliveryItemsMasterDTO> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

    public List<DeliveryItemsMasterDTO> getItems() {
        return items;
    }

    public void setItems(List<DeliveryItemsMasterDTO> items) {
        this.items = items;
    }
}
