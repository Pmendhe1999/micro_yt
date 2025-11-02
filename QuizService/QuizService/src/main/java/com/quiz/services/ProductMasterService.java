package com.quiz.services;

import com.quiz.dto.ProductMasterDTO;
import com.quiz.dto.ProductMasterDTOResponse;
import com.quiz.entities.ProductMaster;
import com.quiz.exception.ResourceNotFoundException;
import com.quiz.mapper.ProductMasterMapper;
import com.quiz.repositories.ProductMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
            Pageable pageable) {

        Page<ProductMaster> page = productMasterRepository.searchProductMastersAdvanced(
                name, productCode, serialNo, orderNo, hsnCode, unit, price, quantity, isPublished, status, pageable);

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

}
