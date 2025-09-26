package com.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AppFunctionDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String status;

    @NotNull(message = "App Function Type ID is required")
    private Long appFunTypesMasterId; // FK to app_fun_types_master

    @NotNull(message = "Application ID is required")
    private Long applicationId; // FK to applications


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public @NotNull(message = "App Function Type ID is required") Long getAppFunTypesMasterId() {
        return appFunTypesMasterId;
    }

    public void setAppFunTypesMasterId(@NotNull(message = "App Function Type ID is required") Long appFunTypesMasterId) {
        this.appFunTypesMasterId = appFunTypesMasterId;
    }

    public @NotNull(message = "Application ID is required") Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(@NotNull(message = "Application ID is required") Long applicationId) {
        this.applicationId = applicationId;
    }
}
