package com.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    @NotBlank(message = "Device ID is required")
    private String deviceId;

    @NotBlank(message = "Device name is required")
    @Size(max = 100, message = "Device name must not exceed 100 characters")
    private String deviceName;

    private String deviceType;

    private String ipAddress;

    private Boolean status;   // e.g., ACTIVE / INACTIVE

    private Set<Long> applicationIds;

    public Set<Long> getApplicationIds() {
        return applicationIds;
    }

    public void setApplicationIds(Set<Long> applicationIds) {
        this.applicationIds = applicationIds;
    }

    public @NotBlank(message = "Device ID is required") String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(@NotBlank(message = "Device ID is required") String deviceId) {
        this.deviceId = deviceId;
    }

    public @NotBlank(message = "Device name is required") @Size(max = 100, message = "Device name must not exceed 100 characters") String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(@NotBlank(message = "Device name is required") @Size(max = 100, message = "Device name must not exceed 100 characters") String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
