package com.qc.dto;

public class QuantitativeCheckRequestDTO {
    private Long quantitativeCheckMasterId;  // 🔥 renamed
    private String name;
    private String description;
    private Boolean isScan;
    private Boolean status;
    private String value;

    public Long getQuantitativeCheckMasterId() {
        return quantitativeCheckMasterId;
    }

    public void setQuantitativeCheckMasterId(Long quantitativeCheckMasterId) {
        this.quantitativeCheckMasterId = quantitativeCheckMasterId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsScan() {
        return isScan;
    }
    public void setIsScan(Boolean isScan) { this.isScan = isScan; }

    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) { this.status = status; }

    public String getValue() {
        return value;
    }
    public void setValue(String value) { this.value = value; }
}
