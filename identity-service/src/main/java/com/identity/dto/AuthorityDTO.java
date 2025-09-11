package com.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDTO {
    @NotBlank(message = "Authority name is required")
    @Size(max = 50, message = "Authority name must not exceed 50 characters")
    private String name;

    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
