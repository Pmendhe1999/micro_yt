package com.qc.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery_challan_master")
public class DeliveryChallanMaster extends AbstractAuditingEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "descriptions", length = 255)
    private String descriptions;

    @Column(name = "status")
    private Boolean status;
    // 🧩 Add this relationship
    @OneToMany(mappedBy = "challan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DeliveryItemsMaster> items = new ArrayList<>();

    // ✅ Convenience method (optional but recommended)
    public void addItem(DeliveryItemsMaster item) {
        items.add(item);
        item.setChallan(this);
    }

    public void removeItem(DeliveryItemsMaster item) {
        items.remove(item);
        item.setChallan(null);
    }

    public List<DeliveryItemsMaster> getItems() {
        return items;
    }

    public void setItems(List<DeliveryItemsMaster> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
