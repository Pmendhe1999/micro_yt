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
    private String name;

    private String description;

    private String status;  // optional field

    public @NotBlank(message = "Application name is required") @Size(max = 100, message = "Application name must not exceed 100 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Application name is required") @Size(max = 100, message = "Application name must not exceed 100 characters") String name) {
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
}
