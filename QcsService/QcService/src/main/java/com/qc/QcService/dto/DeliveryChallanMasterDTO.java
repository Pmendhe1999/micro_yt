package com.qc.QcService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryChallanMasterDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String descriptions;

    @NotNull(message = "Status is required")
    private Boolean status;

    public  String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public  Boolean getStatus() {
        return status;
    }

    public void setStatus( Boolean status) {
        this.status = status;
    }
}
