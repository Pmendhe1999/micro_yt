package com.quiz.services;

import com.quiz.dto.ProductDTO;
import com.quiz.dto.ProductDTOResponse;
import com.quiz.entities.Product;
import com.quiz.entities.QualitativeCheck;
import com.quiz.entities.QuantitativeCheck;
import com.quiz.exception.ResourceNotFoundException;
import com.quiz.mapper.ProductMapper;
import com.quiz.repositories.ProductRepository;
import com.quiz.repositories.QualitativeCheckRepository;
import com.quiz.repositories.QuantitativeCheckRepository;
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

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QualitativeCheckRepository qualitativeCheckRepository;

    @Autowired
    private QuantitativeCheckRepository quantitativeCheckRepository;

    @Autowired
    private ProductMapper productMapper;

    public void createAllProduct(List<ProductDTO> dtoList) {
        List<Product> entities = productMapper.toEntityList(dtoList);

        productRepository.saveAll(entities);
    }

    public void createProduct(ProductDTO dto) {
        Product entity = productMapper.toEntity(dto);



        productRepository.save(entity);
    }

    public Page<ProductDTOResponse> getAllProductsWithFilters(
            String name,
            String productCode,
            String serialNo,
            String orderNo,
            String batchNo,
            String hsnCode,
            String unit,
            BigDecimal price,
            Long inStockQuantity,
            LocalDate mfgDate,
            LocalDate expDate,
            Boolean isPublished,
            Boolean status,
            Pageable pageable) {

        Page<Product> page = productRepository.searchProductsAdvanced(
                name, productCode, serialNo, orderNo, batchNo, hsnCode, unit, price,
                inStockQuantity, mfgDate, expDate, isPublished, status, pageable);

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

        Product updated = productMapper.toEntity(dto);
        updated.setId(existing.getId());
        productRepository.save(updated);
        return productMapper.toDto(updated);
    }

    public ProductDTOResponse patchProduct(Long id, ProductDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if (dto.getBatchNo() != null) existing.setBatchNo(dto.getBatchNo());
        if (dto.getInStockQuantity() != null) existing.setInStockQuantity(dto.getInStockQuantity());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        if (dto.getUnit() != null) existing.setUnit(dto.getUnit());
        if (dto.getHsnCode() != null) existing.setHsnCode(dto.getHsnCode());
        if (dto.getPublished() != null) existing.setPublished(dto.getPublished());


        Product saved = productRepository.save(existing);
        return productMapper.toDto(saved);
    }

    public void deleteProduct(Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(existing);
    }
}
