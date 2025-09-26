package com.identity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_functions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppFunction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    private String status;

    // FK → AppFunTypesMaster
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "app_fun_types_master_id", nullable = false)
    private AppFunTypesMaster appFunTypesMaster;

    // FK → Application
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AppFunTypesMaster getAppFunTypesMaster() {
        return appFunTypesMaster;
    }

    public void setAppFunTypesMaster(AppFunTypesMaster appFunTypesMaster) {
        this.appFunTypesMaster = appFunTypesMaster;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
