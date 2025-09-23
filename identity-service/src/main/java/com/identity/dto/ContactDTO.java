package com.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {

    @NotBlank(message = "Contact person name is required")
    private String contactPerson;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    private String position;

    @NotNull(message = "User ID is required")
    private Long userId;   // maps to UserCredential FK

    public  String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public  String getEmail() {
        return email;
    }

    public void setEmail( String email) {
        this.email = email;
    }

    public  String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public @NotNull(message = "User ID is required") Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull(message = "User ID is required") Long userId) {
        this.userId = userId;
    }
}
