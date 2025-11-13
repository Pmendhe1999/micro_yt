package com.qc.services;

import com.qc.dto.ProductionOrderDTO;
import com.qc.dto.ProductionOrderDTOResponse;
import com.qc.entities.ProductionOrder;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.ProductionOrderMapper;
import com.qc.repositories.ProductionOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
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

    public List<ProductionOrder> uploadProductionOrdersFromExcel(MultipartFile file, String token) {
        List<ProductionOrder> orders = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String name = getCellValue(row.getCell(0));
                String description = getCellValue(row.getCell(1));
                String statusStr = getCellValue(row.getCell(2));
                String priorityStr = getCellValue(row.getCell(3));
                String productionOrderNo = getCellValue(row.getCell(4));

                // Skip empty rows
                if (name == null || name.trim().isEmpty()) continue;

                ProductionOrder order = new ProductionOrder();
                order.setName(name);
                order.setDescription(description);
                order.setStatus(Boolean.parseBoolean(statusStr));
                order.setPriority(parseLongValue(priorityStr));
                order.setProductionOrderNo(productionOrderNo);

                orders.add(order);
            }

            productionOrderRepository.saveAll(orders);
            return orders;

        } catch (Exception e) {
            throw new RuntimeException("Error processing Excel file: " + e.getMessage(), e);
        }
    }

    // ---------- Helper Methods ----------

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue)) {
                    return String.valueOf((long) numericValue);
                }
                return String.valueOf(numericValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    private Long parseLongValue(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return (long) Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
