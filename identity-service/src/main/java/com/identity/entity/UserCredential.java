package com.identity.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class UserCredential {

    public enum Status {
        PENDING,
        ACTIVE,
        BLOCKED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    // 🔹 Many-to-Many with Application (one user can have many applications)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_application",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "application_id")
    )
    private Set<Application> applications = new HashSet<>();

    @Column(name = "username", nullable = false, length = 100)
    private String username;


    @Column(name = "email", length = 150, unique = true)
    private String email;

    @Column(name = "mobile_number", length = 255)
    private String mobileNumber;  // ✅ replaced phone

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "activated", nullable = false)
    private Boolean activated = false;

    @Column(name = "activation_key", nullable = true)
    private Boolean activationKey = false;


    @Column(name = "auth_status")
    private Boolean authStatus;

    @Column(name = "country", length = 255)
    private String country;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "lang_key", length = 6)
    private String langKey;

    @Column(name = "reset_key", length = 100)
    private String resetKey;

    @Column(name = "reset_date")
    private LocalDateTime resetDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;  // default

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "last_modify_date", nullable = false)
    private LocalDateTime lastModifyDate = LocalDateTime.now();

    // 🔹 Many-to-Many with Authority
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonManagedReference
    private MediaDetails mediaDetails;

    // ✅ New field to indicate if user is self-authenticated
    @Column(name = "self_authentication")
    private Boolean selfAuthentication = false;

    // ✅ New field to indicate if user is self-authenticated
    @Column(name = "otp_authentication")
    private Boolean otpAuthentication = false;

    public Boolean getOtpAuthentication() {
        return otpAuthentication;
    }

    public void setOtpAuthentication(Boolean otpAuthentication) {
        this.otpAuthentication = otpAuthentication;
    }

    public Boolean getSelfAuthentication() {
        return selfAuthentication;
    }

    public void setSelfAuthentication(Boolean selfAuthentication) {
        this.selfAuthentication = selfAuthentication;
    }

    public MediaDetails getMediaDetails() {
        return mediaDetails;
    }

    public void setMediaDetails(MediaDetails mediaDetails) {
        this.mediaDetails = mediaDetails;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public LocalDateTime getResetDate() {
        return resetDate;
    }

    public void setResetDate(LocalDateTime resetDate) {
        this.resetDate = resetDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(LocalDateTime lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}