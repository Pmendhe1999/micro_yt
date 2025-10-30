package com.quiz.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "label_scan_master")
public class LabelScanMaster extends AbstractAuditingEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "scan_type", length = 255)
    private String scanType;

    @Column(name = "seq_number")
    private Long seqNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_status", length = 50)
    private CheckStatus checkStatus;

    @Column(name = "status", length = 255)
    private String status;

    public enum CheckStatus {
        QUALITATIVE,
        QUANTITATIVE
    }
}
