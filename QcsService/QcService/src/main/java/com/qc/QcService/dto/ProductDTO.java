package com.qc.QcService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String batchNo;
    private LocalDate expDate;
    private String hsnCode;
    private Long inStockQuantity;
    private Boolean isPublished;
    private LocalDate mfgDate;
    private String name;
    private String orderNo;
    private BigDecimal price;
    private String productCode;
    private String serialNo;
    private Boolean status;
    private String unit;

    private Long qualitativeCheckId;
    private Long quantitativeCheckId;


}
