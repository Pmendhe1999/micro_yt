package com.qc.services;

import com.qc.dto.DeliveryChallanMasterDTO;
import com.qc.dto.DeliveryChallanMasterDTOResponse;
import com.qc.entities.DeliveryChallanMaster;
import com.qc.entities.DeliveryItemsMaster;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.DeliveryChallanMasterMapper;
import com.qc.repositories.DeliveryChallanMasterRepository;
import com.qc.repositories.DeliveryItemsMasterRepository;
import jakarta.transaction.Transactional;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeliveryChallanMasterService {
    @Autowired
    private DeliveryChallanMasterRepository repository;

    @Autowired
    private DeliveryChallanMasterMapper mapper;

    @Autowired
    private DeliveryItemsMasterRepository itemsRepo;

    public void createAllDeliveryChallanMaster(List<DeliveryChallanMasterDTO> dtoList) {
        List<DeliveryChallanMaster> entities = mapper.toEntityList(dtoList);
        repository.saveAll(entities);
    }

    public void createDeliveryChallanMaster(DeliveryChallanMasterDTO dto) {
        DeliveryChallanMaster entity = mapper.toEntity(dto);
        repository.save(entity);
    }

    public void createDeliveryChallanWithItems(DeliveryChallanMasterDTO dto) {
        // 1️⃣ Create DeliveryChallanMaster
        DeliveryChallanMaster challan = new DeliveryChallanMaster();
        challan.setName(dto.getName());
        challan.setDescriptions(dto.getDescriptions());
        challan.setStatus(dto.getStatus() != null ? dto.getStatus() : true);

        challan = repository.save(challan);

        // ✅ Create final reference for lambda
        final DeliveryChallanMaster savedChallan = challan;

        // 2️⃣ Create DeliveryItems for this challan
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            List<DeliveryItemsMaster> items = dto.getItems().stream()
                    .map(itemDto -> {
                        DeliveryItemsMaster item = new DeliveryItemsMaster();
                        item.setBatchNo(itemDto.getBatchNo());
                        item.setDescription(itemDto.getDescription());
                        item.setExpDate(itemDto.getExpDate());
                        item.setHsnCode(itemDto.getHsnCode());
                        item.setMfgDate(itemDto.getMfgDate());
                        item.setName(itemDto.getName());
                        item.setOrderNo(itemDto.getOrderNo());
                        item.setProductCode(itemDto.getProductCode());
                        item.setQuantity(itemDto.getQuantity());
                        item.setSerialNo(itemDto.getSerialNo());
                        item.setUnit(itemDto.getUnit());
                        item.setChallan(savedChallan);
                        return item;
                    })
                    .collect(Collectors.toList());

            itemsRepo.saveAll(items);
        }

    }

    public Page<DeliveryChallanMasterDTOResponse> getAllDeliveryChallanMaster(
            String name,
            String descriptions,
            Boolean status,
            Pageable pageable) {

        Page<DeliveryChallanMaster> page =
                repository.searchDeliveryChallans(name, descriptions, status, pageable);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Delivery Challans found");
        }

        List<DeliveryChallanMasterDTOResponse> dtoList = mapper.toDtoList(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public DeliveryChallanMasterDTOResponse getDeliveryChallanMasterById(Long id) {
        DeliveryChallanMaster entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));
        return mapper.toDto(entity);
    }

    public DeliveryChallanMasterDTOResponse updateDeliveryChallanMaster(Long id, DeliveryChallanMasterDTO dto) {
        DeliveryChallanMaster existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescriptions(dto.getDescriptions());
        existing.setStatus(dto.getStatus());

        DeliveryChallanMaster saved = repository.save(existing);
        return mapper.toDto(saved);
    }

    public DeliveryChallanMasterDTOResponse patchDeliveryChallanMaster(Long id, DeliveryChallanMasterDTO dto) {
        DeliveryChallanMaster existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getDescriptions() != null) existing.setDescriptions(dto.getDescriptions());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());

        DeliveryChallanMaster saved = repository.save(existing);
        return mapper.toDto(saved);
    }

    public void deleteDeliveryChallanMaster(Long id) {
        DeliveryChallanMaster existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));
        repository.delete(existing);
    }

    public List<DeliveryChallanMaster> uploadDeliveryChallansFromExcel(MultipartFile file) {
        Map<String, DeliveryChallanMaster> challanMap = new LinkedHashMap<>(); // Use LinkedHashMap to preserve order

        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String challanName = getCellValue(row.getCell(0));
                String description = getCellValue(row.getCell(1));
                String statusStr = getCellValue(row.getCell(2));

                // ✅ Create or reuse existing challan
                DeliveryChallanMaster challan = challanMap.computeIfAbsent(challanName, name -> {
                    DeliveryChallanMaster c = new DeliveryChallanMaster();
                    c.setName(challanName);
                    c.setDescriptions(description);
                    c.setStatus(Boolean.parseBoolean(statusStr));
                    c.setItems(new ArrayList<>());
                    return c;
                });

                // ✅ Create DeliveryItem for this challan
                DeliveryItemsMaster item = new DeliveryItemsMaster();
                item.setBatchNo(getCellValue(row.getCell(3)));
                item.setDescription(getCellValue(row.getCell(4)));
                item.setExpDate(parseDate(getCellValue(row.getCell(5))));
                item.setMfgDate(parseDate(getCellValue(row.getCell(6))));
                item.setName(getCellValue(row.getCell(7)));
                item.setOrderNo(getCellValue(row.getCell(8)));
                item.setProductCode(getCellValue(row.getCell(9)));
                item.setQuantity(parseBigDecimalValue(getCellValue(row.getCell(10))));
                item.setSerialNo(getCellValue(row.getCell(11)));
                item.setUnit(getCellValue(row.getCell(12)));
                item.setHsnCode(getCellValue(row.getCell(13)));
                item.setChallan(challan);
                challan.addItem(item);
            }

            // ✅ Save all challans with items
            List<DeliveryChallanMaster> savedChallans = repository.saveAll(challanMap.values());
            return savedChallans;

        } catch (Exception e) {
            throw new RuntimeException("Error processing Excel file: " + e.getMessage(), e);
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue().toLocalDate().toString()
                    : BigDecimal.valueOf(cell.getNumericCellValue()).stripTrailingZeros().toPlainString();
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        return LocalDate.parse(dateStr);
    }

    private BigDecimal parseBigDecimalValue(String val) {
        if (val == null || val.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(val);
    }
}
