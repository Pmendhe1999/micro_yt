package com.identity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "auth_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthType {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "status", length = 255)
    private Boolean status;

    // FK → AppFunction
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "app_func_id", nullable = false)
    private AppFunction appFunction;

    // FK → AuthTypeMaster
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "auth_type_master_id", nullable = false)
    private AuthTypeMaster authTypeMaster;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public AppFunction getAppFunction() {
        return appFunction;
    }

    public void setAppFunction(AppFunction appFunction) {
        this.appFunction = appFunction;
    }

    public AuthTypeMaster getAuthTypeMaster() {
        return authTypeMaster;
    }

    public void setAuthTypeMaster(AuthTypeMaster authTypeMaster) {
        this.authTypeMaster = authTypeMaster;
    }
}
