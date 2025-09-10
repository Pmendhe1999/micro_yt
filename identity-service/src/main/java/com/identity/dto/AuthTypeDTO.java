package com.identity.dto;

import com.identity.entity.AuthTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTypeDTO {
    @NotBlank(message = "Auth type name is required")
    @Size(max = 50, message = "Auth type name must not exceed 50 characters")
    private String authTypeName;

    @NotNull(message = "Status is required")
    private AuthTypes.Status status;

    @NotNull(message = "Functionality is required")
    private AuthTypes.Functionality functionality;

    private String description;


    public String getAuthTypeName() {
        return authTypeName;
    }

    public void setAuthTypeName(String authTypeName) {
        this.authTypeName = authTypeName;
    }

    // Getter and Setter for status
    public AuthTypes.Status getStatus() {
        return status;
    }

    public void setStatus(AuthTypes.Status status) {
        this.status = status;
    }

    // Getter and Setter for functionality
    public AuthTypes.Functionality getFunctionality() {
        return functionality;
    }

    public void setFunctionality(AuthTypes.Functionality functionality) {
        this.functionality = functionality;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
