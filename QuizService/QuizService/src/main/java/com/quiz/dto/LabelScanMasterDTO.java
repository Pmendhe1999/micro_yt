package com.quiz.dto;

import com.quiz.entities.LabelScanMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelScanMasterDTO {
    private Long id;
    private String name;
    private String description;
    private String scanType;
    private Long seqNumber;
    private LabelScanMaster.CheckStatus checkStatus;
    private String status;
}
