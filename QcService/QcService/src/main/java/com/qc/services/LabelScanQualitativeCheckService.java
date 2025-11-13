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

        // Step 3️⃣ - Fetch all DeliveryItems for the given DeliveryChallan
        List<DeliveryItems> deliveryItems = deliveryItemsRepository.findByChallanId(deliveryChallan.getId());
        if (deliveryItems.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No DeliveryItems found for DeliveryChallan ID: " + deliveryChallan.getId());
        }

        // Get first DeliveryItem
        DeliveryItems firstDeliveryItem = deliveryItems.get(0);

        // Step 4️⃣ - Iterate through each QualitativeCheck JSON object
        List<QualitativeCheckRequestDTO> checkList = request.getQualitativeChecks();
        for (QualitativeCheckRequestDTO dto : checkList) {

            // Fetch QualitativeCheckMaster
            QualitativeCheckMaster checkMaster = qualitativeCheckMasterRepository
                    .findById(dto.getQualitativeCheckMasterId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "QualitativeCheckMaster not found with ID: " + dto.getQualitativeCheckMasterId()));

            // Fetch ProductMaster by name
            ProductMaster productMaster = productMasterRepository.findByName(dto.getProductName())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "ProductMaster not found with name: " + dto.getProductName()));

            // Step 5️⃣ - Create and populate Product entity
            Product product = new Product();

            // Copy common fields from ProductMaster → Product
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

            // Set foreign keys
            product.setProductMaster(productMaster);
            product.setDeliveryChallan(deliveryChallan);
            product.setDeliveryItems(firstDeliveryItem); // link first DeliveryItem

            // Save the product
            Product savedProduct = productRepository.save(product);


        }
    }
}
