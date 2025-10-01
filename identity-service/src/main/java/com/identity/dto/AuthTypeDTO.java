package com.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTypeDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private Boolean status;

    @NotNull(message = "App Function ID is required")
    private Long appFuncId;

    @NotNull(message = "Auth Type Master ID is required")
    private Long authTypeMasterId;

    public @NotBlank(message = "Name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name is required") String name) {
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

    public  Long getAppFuncId() {
        return appFuncId;
    }

    public void setAppFuncId( Long appFuncId) {
        this.appFuncId = appFuncId;
    }

    public  Long getAuthTypeMasterId() {
        return authTypeMasterId;
    }

    public void setAuthTypeMasterId( Long authTypeMasterId) {
        this.authTypeMasterId = authTypeMasterId;
    }
}
