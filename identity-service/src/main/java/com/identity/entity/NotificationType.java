package com.identity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationType {

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

    // FK → NotificationTechDetails
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_tech_details_id", nullable = false)
    private NotificationTechDetails notificationTechDetails;

    // FK → NotificationTypesMaster
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_type_master_id", nullable = false)
    private NotificationTypesMaster notificationTypesMaster;


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

    public NotificationTechDetails getNotificationTechDetails() {
        return notificationTechDetails;
    }

    public void setNotificationTechDetails(NotificationTechDetails notificationTechDetails) {
        this.notificationTechDetails = notificationTechDetails;
    }

    public NotificationTypesMaster getNotificationTypesMaster() {
        return notificationTypesMaster;
    }

    public void setNotificationTypesMaster(NotificationTypesMaster notificationTypesMaster) {
        this.notificationTypesMaster = notificationTypesMaster;
    }
}
