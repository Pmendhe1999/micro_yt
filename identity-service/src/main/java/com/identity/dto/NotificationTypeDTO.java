package com.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTypeDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String status;

    @NotNull(message = "App Function ID is required")
    private Long appFunctionId;

    @NotNull(message = "Notification Tech Details ID is required")
    private Long notificationTechDetailsId;

    @NotNull(message = "Notification Type Master ID is required")
    private Long notificationTypesMasterId;


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

    public Long getAppFunctionId() {
        return appFunctionId;
    }

    public void setAppFunctionId(Long appFunctionId) {
        this.appFunctionId = appFunctionId;
    }

    public  Long getNotificationTechDetailsId() {
        return notificationTechDetailsId;
    }

    public void setNotificationTechDetailsId(Long notificationTechDetailsId) {
        this.notificationTechDetailsId = notificationTechDetailsId;
    }

    public  Long getNotificationTypesMasterId() {
        return notificationTypesMasterId;
    }

    public void setNotificationTypesMasterId( Long notificationTypesMasterId) {
        this.notificationTypesMasterId = notificationTypesMasterId;
    }
}
