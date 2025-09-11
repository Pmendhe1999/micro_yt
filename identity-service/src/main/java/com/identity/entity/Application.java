package com.identity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "applications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "application_name", nullable = false, length = 100)
    private String applicationName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "auth_type_id", nullable = false)
    private AuthTypes authType;   // ✅ foreign key to auth_types(auth_type_id)

    @Column(columnDefinition = "TEXT")
    private String description;


    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public AuthTypes getAuthType() {
        return authType;
    }

    public void setAuthType(AuthTypes authType) {
        this.authType = authType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
