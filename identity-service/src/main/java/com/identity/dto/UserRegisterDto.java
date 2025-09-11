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

        @NotNull(message = "Application ID is required")
        private Long applicationId;

        @NotBlank(message = "Username is required")
        @Size(max = 100, message = "Username must not exceed 100 characters")
        private String username;

        @Email(message = "Invalid email format")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        private String email;

        @Size(max = 20, message = "Phone must not exceed 20 characters")
        private String phone;

        @NotBlank(message = "Password is required")
        @Size(min = 2, max = 255, message = "Password must be between 6 and 255 characters")
        private String password;

        private String status;     // "PENDING", "ACTIVE", "BLOCKED"

        private Set<Long> authorities; // IDs of Authority table

        public Long getUserId() {
                return userId;
        }

        public void setUserId(Long userId) {
                this.userId = userId;
        }

        public  Long getApplicationId() {
                return applicationId;
        }

        public void setApplicationId( Long applicationId) {
                this.applicationId = applicationId;
        }

        public  String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail( String email) {
                this.email = email;
        }

        public String getPhone() {
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        public  String getPassword() {
                return password;
        }

        public void setPassword( String password) {
                this.password = password;
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
