package com.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTO {

    @NotBlank(message = "Application name is required")
    @Size(max = 100, message = "Application name must not exceed 100 characters")
    private String applicationName;

    @NotNull(message = "Auth type is required")
    private Long authTypeId;   // ✅ maps to auth_types FK

    private String description;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName( String applicationName) {
        this.applicationName = applicationName;
    }

    public Long getAuthTypeId() {
        return authTypeId;
    }

    public void setAuthTypeId( Long authTypeId) {
        this.authTypeId = authTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
