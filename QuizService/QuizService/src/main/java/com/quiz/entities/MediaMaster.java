package com.quiz.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "media_master")
public class MediaMaster extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_image_url", length = 255)
    private String baseImageUrl;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "type", length = 255)
    private String type;

    @Column(name = "uploaded_by", length = 255)
    private String uploadedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_master_id")
    private ProductMaster product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseImageUrl() {
        return baseImageUrl;
    }

    public void setBaseImageUrl(String baseImageUrl) {
        this.baseImageUrl = baseImageUrl;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public ProductMaster getProduct() {
        return product;
    }

    public void setProduct(ProductMaster product) {
        this.product = product;
    }
}
