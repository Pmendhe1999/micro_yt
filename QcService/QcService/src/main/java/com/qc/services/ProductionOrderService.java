package com.qc.services;

import com.qc.dto.ProductionOrderDTO;
import com.qc.dto.ProductionOrderDTOResponse;
import com.qc.entities.ProductionOrder;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.ProductionOrderMapper;
import com.qc.repositories.ProductionOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductionOrderService {

    @Autowired
    private ProductionOrderRepository productionOrderRepository;

    @Autowired
    private ProductionOrderMapper productionOrderMapper;

    public void createAllProductionOrders(List<ProductionOrderDTO> dtoList) {
        List<ProductionOrder> entities = productionOrderMapper.toEntityList(dtoList);
        productionOrderRepository.saveAll(entities);
    }

    public void createProductionOrder(ProductionOrderDTO dto) {
        ProductionOrder entity = productionOrderMapper.toEntity(dto);
        productionOrderRepository.save(entity);
    }

    public Page<ProductionOrderDTOResponse> getAllProductionOrdersWithFilters(
            String name, String description, Boolean status,
            Long priority, String productionOrderNo, Pageable pageable) {

        Page<ProductionOrder> page = productionOrderRepository.searchProductionOrders(
                name, description, status, priority, productionOrderNo, pageable);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Production Orders found");
        }

        List<ProductionOrderDTOResponse> dtoList = productionOrderMapper.toDtoList(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public ProductionOrderDTOResponse getProductionOrderById(Long id) {
        ProductionOrder order = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production Order not found with id: " + id));

        return productionOrderMapper.toDto(order);
    }

    public ProductionOrderDTOResponse updateProductionOrder(Long id, ProductionOrderDTO dto) {
        ProductionOrder existing = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production Order not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setPriority(dto.getPriority());
        existing.setProductionOrderNo(dto.getProductionOrderNo());

        ProductionOrder saved = productionOrderRepository.save(existing);
        return productionOrderMapper.toDto(saved);
    }

    public ProductionOrderDTOResponse patchProductionOrder(Long id, ProductionOrderDTO dto) {
        ProductionOrder existing = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production Order not found with id: " + id));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        if (dto.getPriority() != null) existing.setPriority(dto.getPriority());
        if (dto.getProductionOrderNo() != null) existing.setProductionOrderNo(dto.getProductionOrderNo());

        ProductionOrder saved = productionOrderRepository.save(existing);
        return productionOrderMapper.toDto(saved);
    }

    public void deleteProductionOrder(Long id) {
        ProductionOrder existing = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production Order not found with id: " + id));

        productionOrderRepository.delete(existing);
    }
}
