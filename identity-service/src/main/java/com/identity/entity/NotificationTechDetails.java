package com.identity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_tech_details")

@AllArgsConstructor
@NoArgsConstructor
public class NotificationTechDetails {
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

    @Column(name = "notes", length = 255)
    private String notes;

    @Column(name = "status", length = 255)
    private Boolean status;

    // 🔹 Many tech details belong to one service provider
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_master_id", nullable = false)
    private ServiceProviderMaster serviceProviderMaster;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ServiceProviderMaster getServiceProviderMaster() {
        return serviceProviderMaster;
    }

    public void setServiceProviderMaster(ServiceProviderMaster serviceProviderMaster) {
        this.serviceProviderMaster = serviceProviderMaster;
    }
}
