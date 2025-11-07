package com.qc.services;

import com.qc.dto.ProductMasterDTO;
import com.qc.dto.ProductMasterDTOResponse;
import com.qc.entities.ProductMaster;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.ProductMasterMapper;
import com.qc.repositories.ProductMasterRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ProductMasterService {

    @Autowired
    private  ProductMasterRepository productMasterRepository;
    @Autowired
    private  ProductMasterMapper productMasterMapper;

//    public ProductMasterService(ProductMasterRepository productMasterRepository,
//                                ProductMasterMapper productMasterMapper) {
//        this.productMasterRepository = productMasterRepository;
//        this.productMasterMapper = productMasterMapper;
//    }

    public void createAllProductMaster(List<ProductMasterDTO> productMasterDTOList) {
        List<ProductMaster> entities = productMasterMapper
                .productMasterDTOListToProductMasterList(productMasterDTOList);
        System.out.println(entities);
        productMasterRepository.saveAll(entities);
    }

    public void createProductMaster(ProductMasterDTO productMasterDTO) {
        ProductMaster result = productMasterMapper.productMasterDTOToProductMaster(productMasterDTO);
        productMasterRepository.save(result);
    }

    public Page<ProductMasterDTOResponse> getAllProductMastersWithFilters(
            String name,
            String productCode,
            String serialNo,
            String orderNo,
            String hsnCode,
            String unit,
            BigDecimal price,
            BigDecimal quantity,
            Boolean isPublished,
            Boolean status,
            LocalDate mfgDate,
            LocalDate expDate,
            Pageable pageable) {

        Page<ProductMaster> page = productMasterRepository.searchProductMastersAdvanced(
                name, productCode, serialNo, orderNo, hsnCode, unit, price, quantity,
                isPublished, status, mfgDate, expDate, pageable);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Product Masters found");
        }

        List<ProductMasterDTOResponse> dtoList =
                productMasterMapper.productMasterListToProductMasterDTOListResponse(page.getContent());

        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public ProductMasterDTOResponse getProductMasterById(Long id) {
        ProductMaster productMaster = productMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Master not found with id: " + id));

        return productMasterMapper.productMasterToProductMasterDTOResponse(productMaster);
    }

    public ProductMasterDTOResponse updateProductMaster(Long id, ProductMasterDTO dto) {
        ProductMaster existing = productMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Master not found with id: " + id));

        existing.setName(dto.getName());
        existing.setProductCode(dto.getProductCode());
        existing.setSerialNo(dto.getSerialNo());
        existing.setOrderNo(dto.getOrderNo());
        existing.setHsnCode(dto.getHsnCode());
        existing.setUnit(dto.getUnit());
        existing.setPrice(dto.getPrice());
        existing.setQuantity(dto.getQuantity());
        existing.setInStockQuantity(dto.getInStockQuantity());
        existing.setMfgDate(dto.getMfgDate());
        existing.setExpDate(dto.getExpDate());
        existing.setPublished(dto.getPublished());
        existing.setStatus(dto.getStatus());

        ProductMaster saved = productMasterRepository.save(existing);

        return productMasterMapper.productMasterToProductMasterDTOResponse(saved);
    }

    public ProductMasterDTOResponse patchProductMaster(Long id, ProductMasterDTO dto) {
        ProductMaster existing = productMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Master not found with id: " + id));

        if (dto.getName() != null)
            existing.setName(dto.getName());
        if (dto.getProductCode() != null)
            existing.setProductCode(dto.getProductCode());
        if (dto.getSerialNo() != null)
            existing.setSerialNo(dto.getSerialNo());
        if (dto.getOrderNo() != null)
            existing.setOrderNo(dto.getOrderNo());
        if (dto.getHsnCode() != null)
            existing.setHsnCode(dto.getHsnCode());
        if (dto.getUnit() != null)
            existing.setUnit(dto.getUnit());
        if (dto.getPrice() != null)
            existing.setPrice(dto.getPrice());
        if (dto.getQuantity() != null)
            existing.setQuantity(dto.getQuantity());
        if (dto.getInStockQuantity() != null)
            existing.setInStockQuantity(dto.getInStockQuantity());
        if (dto.getMfgDate() != null)
            existing.setMfgDate(dto.getMfgDate());
        if (dto.getExpDate() != null)
            existing.setExpDate(dto.getExpDate());
        if (dto.getPublished() != null)
            existing.setPublished(dto.getPublished());
        if (dto.getStatus() != null)
            existing.setStatus(dto.getStatus());

        ProductMaster saved = productMasterRepository.save(existing);

        return productMasterMapper.productMasterToProductMasterDTOResponse(saved);
    }
    public void deleteProductMaster(Long id) {
        ProductMaster existing = productMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Master not found with id: " + id));

        productMasterRepository.delete(existing);
    }


    public List<ProductMaster> uploadProductsFromExcel(MultipartFile file, String token) {
        List<ProductMaster> products = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String name = getCellValue(row.getCell(0));
                String descriptionsJson = getCellValue(row.getCell(1)); // Comma-separated list
                String productCode = getCellValue(row.getCell(2));
                String serialNo = getCellValue(row.getCell(3));
                String orderNo = getCellValue(row.getCell(4));
                String hsnCode = getCellValue(row.getCell(5));
                String unit = getCellValue(row.getCell(6));
                String priceStr = getCellValue(row.getCell(7));
                String quantityStr = getCellValue(row.getCell(8));
                String inStockQuantityStr = getCellValue(row.getCell(9));
                String mfgDateStr = getCellValue(row.getCell(10));
                String expDateStr = getCellValue(row.getCell(11));
                String isPublishedStr = getCellValue(row.getCell(12));
                String statusStr = getCellValue(row.getCell(13));

                if (name == null || name.trim().isEmpty()) continue;

                ProductMaster product = new ProductMaster();
                product.setName(name);
                product.setDescriptions(
                        descriptionsJson != null ? Arrays.asList(descriptionsJson.split(",")) : null);
                product.setProductCode(productCode);
                product.setSerialNo(serialNo);
                product.setOrderNo(orderNo);
                product.setHsnCode(hsnCode);
                product.setUnit(unit);
                product.setPrice(parseBigDecimalValue(priceStr));
                product.setQuantity(parseBigDecimalValue(quantityStr));
                product.setInStockQuantity(parseLongValue(inStockQuantityStr));
                product.setMfgDate(parseDate(mfgDateStr));
                product.setExpDate(parseDate(expDateStr));
                product.setPublished(Boolean.parseBoolean(isPublishedStr));
                product.setStatus(Boolean.parseBoolean(statusStr));

                products.add(product);
            }

            productMasterRepository.saveAll(products);
            return products;

        } catch (Exception e) {
            throw new RuntimeException("Error processing Excel file: " + e.getMessage(), e);
        }
    }

    // ✅ Helper methods
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
                    return String.valueOf((long) numericValue); // avoid .0
                }
                return String.valueOf(numericValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    private BigDecimal parseBigDecimalValue(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
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

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

}
