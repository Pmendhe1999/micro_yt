package com.qc.QcService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "qualitative_check_master")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualitativeCheckMaster {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_status", length = 50)
    private CheckStatus checkStatus;

    @Column(name = "status", length = 255)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scan_master_id", nullable = false)
    private LabelScanMaster scanMaster;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    public enum CheckStatus {
        GOODTOHAVE,
        MUSTHAVE
    }
}
