package com.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class UserRegisterDto {

        @NotBlank(message = "Name is required")
        private String name;

        @Email(message = "Invalid email")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        @NotBlank(message = "Role is required")
        private String role;

        private Set<Long> authorities; // IDs of Authority table

        // --- Getters & Setters ---
        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getRole() {
                return role;
        }

        public void setRole(String role) {
                this.role = role;
        }

        public Set<Long> getAuthorities() {
                return authorities;
        }

        public void setAuthorities(Set<Long> authorities) {
                this.authorities = authorities;
        }

}
