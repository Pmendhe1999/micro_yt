package com.qc.services;

import com.qc.dto.ProductDTO;
import com.qc.dto.ProductDTOResponse;
import com.qc.entities.DeliveryChallan;
import com.qc.entities.DeliveryItemsMaster;
import com.qc.entities.Product;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.ProductMapper;
import com.qc.repositories.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductMapper productMapper;

    @Autowired private DeliveryChallanRepository deliveryChallanRepository;
    @Autowired private DeliveryItemsMasterRepository deliveryItemsMasterRepository;

    public void createAllProduct(List<ProductDTO> dtoList) {
        List<Product> entities = productMapper.toEntityList(dtoList);
        productRepository.saveAll(entities);
    }

    public ProductDTOResponse createProduct(ProductDTO dto) {
        Product entity = productMapper.toEntity(dto);

        // Attach referenced entities properly
        if (dto.getDeliveryChallanId() != null) {
            DeliveryChallan challan = deliveryChallanRepository.findById(dto.getDeliveryChallanId())
                    .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found"));
            entity.setDeliveryChallan(challan);
        }

        if (dto.getDeliveryItemId() != null) {
            DeliveryItemsMaster item = deliveryItemsMasterRepository.findById(dto.getDeliveryItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Delivery Item not found"));
            entity.setDeliveryItem(item);
        }

        Product saved = productRepository.save(entity);
        return productMapper.toDto(saved);
    }

    public Page<ProductDTOResponse> getAllProductsWithFilters(
            String name, String productCode, String serialNo, String orderNo,
            String batchNo, String hsnCode, String unit, BigDecimal price, Long inStockQuantity,
            LocalDate mfgDate, LocalDate expDate, Boolean isPublished, Boolean status,
            Long deliveryChallanId, Long deliveryItemId,
            Pageable pageable) {

        Page<Product> page = productRepository.searchProductsAdvanced(
                name, productCode, serialNo, orderNo, batchNo, hsnCode, unit, price,
                inStockQuantity, mfgDate, expDate, isPublished, status,
                deliveryChallanId, deliveryItemId, pageable);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Products found");
        }

        List<ProductDTOResponse> dtoList = productMapper.toDtoList(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public ProductDTOResponse getProductById(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(entity);
    }

    public ProductDTOResponse updateProduct(Long id, ProductDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Map updated fields
        Product updated = productMapper.toEntity(dto);
        updated.setId(existing.getId());

        // Update linked entities
        if (dto.getDeliveryChallanId() != null) {
            DeliveryChallan challan = deliveryChallanRepository.findById(dto.getDeliveryChallanId())
                    .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found"));
            updated.setDeliveryChallan(challan);
        }

        if (dto.getDeliveryItemId() != null) {
            DeliveryItemsMaster item = deliveryItemsMasterRepository.findById(dto.getDeliveryItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Delivery Item not found"));
            updated.setDeliveryItem(item);
        }

        Product saved = productRepository.save(updated);
        return productMapper.toDto(saved);
    }

    public void deleteProduct(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(entity);
    }
}
