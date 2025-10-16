package com.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CountryDTO {
    @NotBlank(message = "Country name is required")
    @Size(max = 100, message = "Country name must not exceed 100 characters")
    private String countryName;

    @Size(max = 10, message = "Country code must not exceed 10 characters")
    private String countryCode;

    private String description;

    private Boolean status;

    public @NotBlank(message = "Country name is required") @Size(max = 100, message = "Country name must not exceed 100 characters") String getCountryName() {
        return countryName;
    }

    public void setCountryName(@NotBlank(message = "Country name is required") @Size(max = 100, message = "Country name must not exceed 100 characters") String countryName) {
        this.countryName = countryName;
    }

    public @Size(max = 10, message = "Country code must not exceed 10 characters") String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(@Size(max = 10, message = "Country code must not exceed 10 characters") String countryCode) {
        this.countryCode = countryCode;
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
