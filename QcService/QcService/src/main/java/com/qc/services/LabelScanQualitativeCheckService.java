package com.qc.services;

import com.qc.dto.*;
import com.qc.entities.*;
import com.qc.exception.ResourceNotFoundException;
import com.qc.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        String size = null;
        String orientation = null;

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
        final String finalSize = size;
        final String finalOrientation = orientation;

        // Step 4️⃣ - Fetch ProductMaster by name and productCode
        ProductMaster productMaster = productMasterRepository
                .findByNameAndProductCodeAndSizeAndOrientation(finalProductName, finalProductCode,finalSize,finalOrientation)
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

        // Step 8️⃣ - Create and save QualitativeCheck records for each qualitative check DTO
        //Quantitative aahe he
        List<QualitativeCheck> qualitativeChecks = new ArrayList<>();

        for (QualitativeCheckRequestDTO dto : request.getQualitativeChecks()) {
            QualitativeCheckMaster master = qualitativeCheckMasterRepository
                    .findById(dto.getQualitativeCheckMasterId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "QualitativeCheckMaster not found with ID: " + dto.getQualitativeCheckMasterId()));

            QualitativeCheck check = new QualitativeCheck();
            check.setDescription(dto.getDescription());
            check.setScan(dto.getScan());
            check.setStatus(dto.getStatus());// true or false
            check.setValue(dto.getValue());
            check.setQualitativeCheckMaster(master);
            check.setProduct(product); // ✅ link to saved Product

            qualitativeChecks.add(check);
        }

        qualitativeCheckRepository.saveAll(qualitativeChecks);

    }

    @Autowired
    private QuantitativeCheckMasterRepository quantitativeCheckMasterRepository;
    @Autowired
    private QuantitativeCheckRepository quantitativeCheckRepository;
    public LabelScanQuantitativeCheckResponseDTO createQuantitativeChecks(
            LabelScanQuantitativeCheckRequestDTO request) {

        // Step 1 - Validate LabelScanMaster
        LabelScanMaster labelScanMaster =
                labelScanMasterRepository.findById(request.getLabelScanMasterId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "LabelScanMaster not found with ID: " + request.getLabelScanMasterId()));

        // Step 2 - Validate DeliveryChallan
        DeliveryChallan deliveryChallan =
                deliveryChallanRepository.findById(request.getDeliveryChallanId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "DeliveryChallan not found with ID: " + request.getDeliveryChallanId()));


        // Step 3 - Extract required product fields
        String productName = null;
        String productCode = null;
        String size = null;
        String orientation = null;

        for (QuantitativeCheckRequestDTO dto : request.getQuantitativeChecks()) {
            switch (dto.getName().toLowerCase()) {
                case "productname": productName = dto.getValue(); break;
                case "productcode": productCode = dto.getValue(); break;
                case "size": size = dto.getValue(); break;
                case "orientation": orientation = dto.getValue(); break;
            }
        }

        if (productName == null || productCode == null || size == null || orientation == null) {
            throw new ResourceNotFoundException("productName, productCode, size, orientation all required");
        }

        // FINAL VARIABLES for lambda usage
        final String finalProductName = productName;
        final String finalProductCode = productCode;
        final String finalSize = size;
        final String finalOrientation = orientation;


        // Step 4 - Fetch ProductMaster
        ProductMaster productMaster = productMasterRepository
                .findByNameAndProductCodeAndSizeAndOrientation(
                        finalProductName, finalProductCode, finalSize, finalOrientation)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ProductMaster not found for: " + finalProductName +
                                " | " + finalProductCode + " | " + finalSize + " | " + finalOrientation));


        // Step 5 - Fetch DeliveryItem
        DeliveryItems deliveryItem = deliveryItemsRepository
                .findByNameAndProductCode(finalProductName, finalProductCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "DeliveryItem not found for name: " + finalProductName + " and code: " + finalProductCode));


        // Step 6 - Create Product
        Product product = new Product();

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

        product.setSize(finalSize);
        product.setOrientation(finalOrientation);

        product.setProductMaster(productMaster);
        product.setDeliveryChallan(deliveryChallan);
        product.setDeliveryItems(deliveryItem);

        productRepository.save(product);


        // Step 7 - Save Quantitative Checks
        List<QuantitativeCheck> savedChecks = new ArrayList<>();

        for (QuantitativeCheckRequestDTO dto : request.getQuantitativeChecks()) {

            QuantitativeCheckMaster master = quantitativeCheckMasterRepository
                    .findById(dto.getQuantitativeCheckMasterId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "QuantitativeCheckMaster not found with ID: " + dto.getQuantitativeCheckMasterId()));

            QuantitativeCheck check = new QuantitativeCheck();

            check.setDescription(dto.getDescription());
            check.setScan(dto.getIsScan());
            check.setStatus(dto.getStatus());
            check.setValue(dto.getValue());
            check.setQuantitativeCheckMaster(master);
            check.setProduct(product);

            savedChecks.add(check);
        }

        quantitativeCheckRepository.saveAll(savedChecks);


        // Step 8 - Map to Response DTO
        final Product finalProduct = product;  // <-- for lambda

        List<QuantitativeCheckDTOResponse> responseList = savedChecks.stream()
                .map(check -> new QuantitativeCheckDTOResponse(
                        check.getId(),
                        check.getDescription(),
                        check.getScan(),
                        check.getStatus(),

                        check.getValue(),
                        check.getQuantitativeCheckMaster().getId(),
                        check.getQuantitativeCheckMaster().getName(),
                        finalProduct.getId(),
                        finalProduct.getName(),
                        finalProduct.getProductCode(),
                        finalProduct.getSize(),
                        finalProduct.getOrientation()
                ))
                .collect(Collectors.toList());


        return new LabelScanQuantitativeCheckResponseDTO(
                finalProduct.getId(),
                labelScanMaster.getId(),
                deliveryChallan.getId(),
                responseList
        );
    }

    
    public LabelScanQuantitativeCheckUpdateResponseDTO updateQuantitativeChecks(
            LabelScanQuantitativeCheckUpdateRequestDTO request) {

        // Step 1: Validate LabelScanMaster
        LabelScanMaster labelScanMaster = labelScanMasterRepository.findById(request.getLabelScanMasterId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "LabelScanMaster not found with ID: " + request.getLabelScanMasterId()));

        // Step 2: Validate DeliveryChallan
        DeliveryChallan deliveryChallan = deliveryChallanRepository.findById(request.getDeliveryChallanId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "DeliveryChallan not found with ID: " + request.getDeliveryChallanId()));

        // Step 3: Validate Product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + request.getProductId()));

        // Fetch ProductMaster
        ProductMaster productMaster = product.getProductMaster();

        // Step 4: Extract mfgLifNo & sterileType from request
        String mfgLifNo = null;
        String sterileType = null;

        for (QuantitativeCheckRequestDTO dto : request.getQuantitativeChecks()) {
            switch (dto.getName().toLowerCase()) {
                case "mfglifno": mfgLifNo = dto.getValue(); break;
                case "steriletype": sterileType = dto.getValue(); break;
            }
        }

        // Step 5: Validate with ProductMaster
        if (!Objects.equals(productMaster.getMfgLifNo(), mfgLifNo)) {
            throw new ResourceNotFoundException("mfgLifNo does not match with ProductMaster");
        }

        if (!Objects.equals(productMaster.getSterileType(), sterileType)) {
            throw new ResourceNotFoundException("sterileType does not match with ProductMaster");
        }

        // Step 6: Save Quantitative Checks
        List<QuantitativeCheck> savedChecks = new ArrayList<>();

        for (QuantitativeCheckRequestDTO dto : request.getQuantitativeChecks()) {

            QuantitativeCheckMaster master = quantitativeCheckMasterRepository
                    .findById(dto.getQuantitativeCheckMasterId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "QuantitativeCheckMaster not found with ID: " + dto.getQuantitativeCheckMasterId()));

            QuantitativeCheck check = new QuantitativeCheck();
            check.setDescription(dto.getDescription());
            check.setScan(dto.getIsScan());
            check.setStatus(dto.getStatus());
            check.setValue(dto.getValue());
            check.setQuantitativeCheckMaster(master);
            check.setProduct(product);

            savedChecks.add(check);
        }

        quantitativeCheckRepository.saveAll(savedChecks);

        // Step 7: Prepare Response
        List<QuantitativeCheckDTOResponse> responseList = savedChecks.stream()
                .map(c -> new QuantitativeCheckDTOResponse(
                        c.getId(),
                        c.getDescription(),
                        c.getScan(),
                        c.getStatus(),
                        c.getValue(),
                        c.getQuantitativeCheckMaster().getId(),
                        c.getQuantitativeCheckMaster().getName(),
                        product.getId(),
                        product.getName(),
                        product.getProductCode(),
                        product.getSize(),
                        product.getOrientation()
                ))
                .collect(Collectors.toList());

        // Return DTO
        return new LabelScanQuantitativeCheckUpdateResponseDTO(
                labelScanMaster.getId(),
                deliveryChallan.getId(),
                product.getId(),
                responseList
        );
    }


}
