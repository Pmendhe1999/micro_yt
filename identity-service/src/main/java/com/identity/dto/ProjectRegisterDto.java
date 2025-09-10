package com.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRegisterDto {

    @NotBlank(message = "Project name is required")
    private String name;

    private boolean status = true;

    @NotNull(message = "Authentication type is required")
    private Long authTypeId;

    public  String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getAuthTypeId() {
        return authTypeId;
    }

    public void setAuthTypeId( Long authTypeId) {
        this.authTypeId = authTypeId;
    }
}
