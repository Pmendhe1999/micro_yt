package com.qc.services;

import com.qc.dto.LabelScanQualitativeCheckRequestDTO;
import com.qc.dto.QualitativeCheckRequestDTO;
import com.qc.entities.*;
import com.qc.exception.ResourceNotFoundException;
import com.qc.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LabelScanQualitativeCheckService {

    @Autowired
    private  LabelScanMasterRepository labelScanMasterRepository;
    @Autowired
    private  QualitativeCheckMasterRepository qualitativeCheckMasterRepository;
    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private  ProductMasterRepository productMasterRepository;
    @Autowired
    private  QualitativeCheckRepository qualitativeCheckRepository;
    @Autowired
    private DeliveryChallanRepository deliveryChallanRepository;

    @Autowired
    private DeliveryItemsRepository deliveryItemsRepository;

    public void createQualitativeChecks(LabelScanQualitativeCheckRequestDTO request) {

        // Step 1️⃣ - Validate LabelScanMaster
        LabelScanMaster labelScanMaster = labelScanMasterRepository.findById(request.getLabelScanMasterId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "LabelScanMaster not found with ID: " + request.getLabelScanMasterId()));

        // Step 2️⃣ - Validate DeliveryChallan
        DeliveryChallan deliveryChallan = deliveryChallanRepository.findById(request.getDeliveryChallanId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "DeliveryChallan not found with ID: " + request.getDeliveryChallanId()));

        // Step 3️⃣ - Extract productName and productCode values from qualitativeChecks
        String productName = null;
        String productCode = null;

        for (QualitativeCheckRequestDTO dto : request.getQualitativeChecks()) {
            if ("productName".equalsIgnoreCase(dto.getName())) {
                productName = dto.getValue();
            } else if ("productCode".equalsIgnoreCase(dto.getName())) {
                productCode = dto.getValue();
            }
        }

        if (productName == null || productCode == null) {
            throw new ResourceNotFoundException("Both productName and productCode must be provided in qualitativeChecks");
        }

        final String finalProductName = productName;
        final String finalProductCode = productCode;

        // Step 4️⃣ - Fetch ProductMaster by name and productCode
        ProductMaster productMaster = productMasterRepository
                .findByNameAndProductCode(finalProductName, finalProductCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ProductMaster not found with name: " + finalProductName + " and code: " + finalProductCode));

        // Step 5️⃣ - Find matching DeliveryItem by name and productCode
        DeliveryItems deliveryItem = deliveryItemsRepository
                .findByNameAndProductCode(finalProductName, finalProductCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "DeliveryItem not found with name: " + finalProductName + " and code: " + finalProductCode));

        // Step 6️⃣ - Fetch QualitativeCheckMaster (from first qualitativeCheck)
        QualitativeCheckRequestDTO firstCheck = request.getQualitativeChecks().get(0);
        QualitativeCheckMaster checkMaster = qualitativeCheckMasterRepository
                .findById(firstCheck.getQualitativeCheckMasterId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "QualitativeCheckMaster not found with ID: " + firstCheck.getQualitativeCheckMasterId()));

        // Step 7️⃣ - Create Product entity
        Product product = new Product();

        // Copy fields from ProductMaster → Product
        product.setName(productMaster.getName());
        product.setProductCode(productMaster.getProductCode());
        product.setSerialNo(productMaster.getSerialNo());
        product.setOrderNo(productMaster.getOrderNo());
        product.setHsnCode(productMaster.getHsnCode());
        product.setUnit(productMaster.getUnit());
        product.setPrice(productMaster.getPrice());
        product.setInStockQuantity(productMaster.getInStockQuantity());
        product.setMfgDate(productMaster.getMfgDate());
        product.setExpDate(productMaster.getExpDate());
        product.setPublished(productMaster.getPublished());
        product.setStatus(productMaster.getStatus());

        // Set relationships
        product.setProductMaster(productMaster);
        product.setDeliveryChallan(deliveryChallan);
        product.setDeliveryItems(deliveryItem);

        // Save Product
        productRepository.save(product);

    }


}
