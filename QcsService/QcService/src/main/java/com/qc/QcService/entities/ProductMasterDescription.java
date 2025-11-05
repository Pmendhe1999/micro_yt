package com.qc.QcService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_master_descriptions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMasterDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Optional surrogate key

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_master_id", nullable = false)
    private ProductMaster productMaster;

    @Column(name = "descriptions", length = 255)
    private String descriptions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductMaster getProductMaster() {
        return productMaster;
    }

    public void setProductMaster(ProductMaster productMaster) {
        this.productMaster = productMaster;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}
