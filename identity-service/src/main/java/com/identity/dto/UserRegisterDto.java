package com.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

        private Long userId;

        // 🔹 One user can have many applications
        private Set<Long> applicationIds;

        @NotBlank(message = "Username is required")
        @Size(max = 100, message = "Username must not exceed 100 characters")
        private String username;

        @Email(message = "Invalid email format")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        private String email;

        @Size(max = 255, message = "Mobile number must not exceed 255 characters")
        private String mobileNumber;


        @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
        private String password;

        private Boolean activated;

        private Boolean activationKey;

        private Boolean authStatus;

        private String country;

        @Size(max = 50, message = "First name must not exceed 50 characters")
        private String firstName;

        @Size(max = 50, message = "Last name must not exceed 50 characters")
        private String lastName;

        @Size(max = 6, message = "Lang key must not exceed 6 characters")
        private String langKey;

        private String resetKey;

        private String status;     // "PENDING", "ACTIVE", "BLOCKED"

        private Set<Long> authorities; // IDs of Authority table

        public Long getUserId() {
                return userId;
        }

        public void setUserId(Long userId) {
                this.userId = userId;
        }

        public Set<Long> getApplicationIds() {
                return applicationIds;
        }

        public void setApplicationIds(Set<Long> applicationIds) {
                this.applicationIds = applicationIds;
        }

        public  String getUsername() {
                return username;
        }

        public void setUsername( String username) {
                this.username = username;
        }

        public  String getEmail() {
                return email;
        }

        public void setEmail( String email) {
                this.email = email;
        }

        public String getMobileNumber() {
                return mobileNumber;
        }

        public void setMobileNumber( String mobileNumber) {
                this.mobileNumber = mobileNumber;
        }

        public  String getPassword() {
                return password;
        }

        public void setPassword( String password) {
                this.password = password;
        }

        public Boolean getActivated() {
                return activated;
        }

        public void setActivated(Boolean activated) {
                this.activated = activated;
        }

        public Boolean getActivationKey() {
                return activationKey;
        }

        public void setActivationKey(Boolean activationKey) {
                this.activationKey = activationKey;
        }

        public Boolean getAuthStatus() {
                return authStatus;
        }

        public void setAuthStatus(Boolean authStatus) {
                this.authStatus = authStatus;
        }

        public String getCountry() {
                return country;
        }

        public void setCountry(String country) {
                this.country = country;
        }

        public  String getFirstName() {
                return firstName;
        }

        public void setFirstName( String firstName) {
                this.firstName = firstName;
        }

        public  String getLastName() {
                return lastName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public  String getLangKey() {
                return langKey;
        }

        public void setLangKey( String langKey) {
                this.langKey = langKey;
        }

        public String getResetKey() {
                return resetKey;
        }

        public void setResetKey(String resetKey) {
                this.resetKey = resetKey;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public Set<Long> getAuthorities() {
                return authorities;
        }

        public void setAuthorities(Set<Long> authorities) {
                this.authorities = authorities;
        }
}
