package com.identity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_type_id")
    private Long authTypeId;

    @Column(name = "auth_type_name", nullable = false, unique = true, length = 50)
    private String authTypeName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE'")
    private Status status = Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('LOGIN', 'REGISTRATION', 'BOTH')")
    private Functionality functionality;

    @Column(columnDefinition = "TEXT")
    private String description;

    // 🔹 Enum for status
    public enum Status {
        ACTIVE, INACTIVE
    }

    // 🔹 Enum for functionality
    public enum Functionality {
        LOGIN, REGISTRATION, BOTH
    }


    public Long getAuthTypeId() {
        return authTypeId;
    }

    public void setAuthTypeId(Long authTypeId) {
        this.authTypeId = authTypeId;
    }

    public String getAuthTypeName() {
        return authTypeName;
    }

    public void setAuthTypeName(String authTypeName) {
        this.authTypeName = authTypeName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Functionality getFunctionality() {
        return functionality;
    }

    public void setFunctionality(Functionality functionality) {
        this.functionality = functionality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
