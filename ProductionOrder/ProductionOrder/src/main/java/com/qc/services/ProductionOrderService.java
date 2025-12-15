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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;
    private final ProductionOrderMapper productionOrderMapper;

    public void createAllProductionOrders(List<ProductionOrderDTO> dtoList) {
        List<ProductionOrder> entities = productionOrderMapper.toEntityList(dtoList);
        productionOrderRepository.saveAll(entities);
    }

    public void createProductionOrder(ProductionOrderDTO dto) {
        ProductionOrder entity = productionOrderMapper.toEntity(dto);
        productionOrderRepository.save(entity);
    }

    public Page<ProductionOrderDTOResponse> getAll(
            String productionOrderNo,
            String batchNo,
            String currentWorkCenter,
            String activityNumber,
            String operation,
            Long priority,
            String productCode,
            String productDescription,
            Pageable pageable) {

        Page<ProductionOrder> page = productionOrderRepository.search(
                productionOrderNo, batchNo, currentWorkCenter, activityNumber, operation, priority,productCode,productDescription, pageable);

        List<ProductionOrderDTOResponse> list = productionOrderMapper.toDtoList(page.getContent());

        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    public ProductionOrderDTOResponse getById(Long id) {
        ProductionOrder order = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production Order not found with id " + id));

        return productionOrderMapper.toDto(order);
    }

    public ProductionOrderDTOResponse update(Long id, ProductionOrderDTO dto) {

        ProductionOrder existing = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production Order not found with id " + id));

        existing.setProductionOrderDate(dto.getProductionOrderDate());
        existing.setProductionOrderNo(dto.getProductionOrderNo());
        existing.setBatchNo(dto.getBatchNo());
        existing.setOrderQuantity(dto.getOrderQuantity());
        existing.setCurrentWorkCenter(dto.getCurrentWorkCenter());
        existing.setActivityNumber(dto.getActivityNumber());
        existing.setOperation(dto.getOperation());
        existing.setPriority(dto.getPriority());
        existing.setPriorityRemark(dto.getPriorityRemark());
        existing.setProductCode(dto.getProductCode());
        existing.setProductDescription(dto.getProductDescription());
        ProductionOrder saved = productionOrderRepository.save(existing);
        return productionOrderMapper.toDto(saved);
    }

    public ProductionOrderDTOResponse patch(Long id, ProductionOrderDTO dto) {

        ProductionOrder existing = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production Order not found with id " + id));

        if (dto.getProductionOrderDate() != null) existing.setProductionOrderDate(dto.getProductionOrderDate());
        if (dto.getProductionOrderNo() != null) existing.setProductionOrderNo(dto.getProductionOrderNo());
        if (dto.getBatchNo() != null) existing.setBatchNo(dto.getBatchNo());
        if (dto.getOrderQuantity() != null) existing.setOrderQuantity(dto.getOrderQuantity());
        if (dto.getCurrentWorkCenter() != null) existing.setCurrentWorkCenter(dto.getCurrentWorkCenter());
        if (dto.getActivityNumber() != null) existing.setActivityNumber(dto.getActivityNumber());
        if (dto.getOperation() != null) existing.setOperation(dto.getOperation());
        if (dto.getPriority() != null) existing.setPriority(dto.getPriority());
        if (dto.getPriorityRemark() != null) existing.setPriorityRemark(dto.getPriorityRemark());
        if (dto.getProductCode() != null)
            existing.setProductCode(dto.getProductCode());

        if (dto.getProductDescription() != null)
            existing.setProductDescription(dto.getProductDescription());
        ProductionOrder saved = productionOrderRepository.save(existing);
        return productionOrderMapper.toDto(saved);
    }

    public void delete(Long id) {
        ProductionOrder existing = productionOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production Order not found with id " + id));

        productionOrderRepository.delete(existing);
    }

    public List<ProductionOrder> uploadProductionOrdersFromExcel(MultipartFile file, String token) {

        List<ProductionOrder> orders = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            // ❗ First delete all existing records
            productionOrderRepository.deleteAll();
            // Skip header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                String dateStr = getCellValue(row.getCell(0));
                String productionOrderNo = getCellValue(row.getCell(1));
                String batchNo = getCellValue(row.getCell(2));
                String orderQuantityStr = getCellValue(row.getCell(3));
                String currentWorkCenter = getCellValue(row.getCell(4));
                String activityNumber = getCellValue(row.getCell(5));
                String operation = getCellValue(row.getCell(6));
                String priorityStr = getCellValue(row.getCell(7));
                String priorityRemark = getCellValue(row.getCell(8));
                String productCode = getCellValue(row.getCell(9));
                String productDescription = getCellValue(row.getCell(10));
                // Skip empty rows
                if (productionOrderNo == null || productionOrderNo.trim().isEmpty())
                    continue;

                ProductionOrder order = new ProductionOrder();

                order.setProductionOrderDate(parseLocalDate(dateStr));
                order.setProductionOrderNo(productionOrderNo);
                order.setBatchNo(batchNo);
                order.setOrderQuantity(parseBigDecimal(orderQuantityStr));
                order.setCurrentWorkCenter(currentWorkCenter);
                order.setActivityNumber(activityNumber);
                order.setOperation(operation);
                order.setPriority(parseLong(priorityStr));
                order.setPriorityRemark(priorityRemark);
                order.setProductCode(productCode);
                order.setProductDescription(productDescription);
                orders.add(order);
            }

            productionOrderRepository.saveAll(orders);
            return orders;

        } catch (Exception e) {
            throw new RuntimeException("Error processing Excel file: " + e.getMessage(), e);
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                double num = cell.getNumericCellValue();
                if (num == Math.floor(num)) yield String.valueOf((long) num);
                else yield String.valueOf(num);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }

    private LocalDate parseLocalDate(String value) {
        try {
            return (value == null || value.isEmpty()) ? null : LocalDate.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return (value == null || value.isEmpty()) ? null : new BigDecimal(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Long parseLong(String value) {
        try {
            return (value == null || value.isEmpty()) ? null : Long.parseLong(value.trim());
        } catch (Exception e) {
            return null;
        }
    }
    public List<String> getDistinctCurrentWorkCenters() {
        return productionOrderRepository.findDistinctCurrentWorkCenters();
    }
}
