package com.qc.QcService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_items_master")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryItemsMaster {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_no", length = 255)
    private String batchNo;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "exp_date")
    private LocalDate expDate;

    @Column(name = "hsn_code", length = 255)
    private String hsnCode;

    @Column(name = "mfg_date")
    private LocalDate mfgDate;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "order_no", length = 255)
    private String orderNo;

    @Column(name = "product_code", length = 255)
    private String productCode;

    @Column(name = "quantity", precision = 19, scale = 2)
    private BigDecimal quantity;

    @Column(name = "serial_no", length = 255)
    private String serialNo;

    @Column(name = "unit", length = 255)
    private String unit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challen_id", nullable = false)
    private DeliveryChallanMaster challan;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
