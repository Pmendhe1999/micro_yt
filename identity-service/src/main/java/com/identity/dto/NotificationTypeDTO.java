package com.identity.dto;

import com.identity.entity.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTypeDTO {

    @NotNull(message = "Auth type is required")
    private Long authTypeId;

    @NotBlank(message = "Notification name is required")
    @Size(max = 50, message = "Notification name must not exceed 50 characters")
    private String notificationName;

    private NotificationType.Status status = NotificationType.Status.ACTIVE;

    private String description;


    public  Long getAuthTypeId() {
        return authTypeId;
    }

    public void setAuthTypeId( Long authTypeId) {
        this.authTypeId = authTypeId;
    }

    public  String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public NotificationType.Status getStatus() {
        return status;
    }

    public void setStatus(NotificationType.Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
