package com.qc.services;

import com.qc.dto.ProductDescriptionDTO;
import com.qc.dto.ProductDescriptionDTOResponse;
import com.qc.entities.ProductDescription;
import com.qc.entities.ProductMaster;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.ProductDescriptionMapper;
import com.qc.repositories.ProductDescriptionRepository;
import com.qc.repositories.ProductMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductDescriptionService {
    @Autowired
    private ProductDescriptionRepository productDescriptionRepository;

    @Autowired
    private ProductMasterRepository productMasterRepository;

    @Autowired
    private ProductDescriptionMapper productDescriptionMapper;

    public void createAllProductDescription(List<ProductDescriptionDTO> dtoList) {
        List<ProductDescription> entities = productDescriptionMapper.toEntityList(dtoList);
        entities.forEach(entity -> {
            ProductMaster product = productMasterRepository.findById(entity.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + entity.getProduct().getId()));
            entity.setProduct(product);
        });
        productDescriptionRepository.saveAll(entities);
    }

    public void createProductDescription(ProductDescriptionDTO dto) {
        ProductDescription entity = productDescriptionMapper.toEntity(dto);
        ProductMaster product = productMasterRepository.findById(entity.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + entity.getProduct().getId()));
        entity.setProduct(product);
        productDescriptionRepository.save(entity);
    }

    public Page<ProductDescriptionDTOResponse> getAllProductDescription(Pageable pageable) {
        Page<ProductDescription> page = productDescriptionRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Product Descriptions found");
        }
        List<ProductDescriptionDTOResponse> dtoList = productDescriptionMapper.toDtoList(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public ProductDescriptionDTOResponse getProductDescriptionById(Long id) {
        ProductDescription entity = productDescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Description not found with id: " + id));
        return productDescriptionMapper.toDto(entity);
    }

    public ProductDescriptionDTOResponse updateProductDescription(Long id, ProductDescriptionDTO dto) {
        ProductDescription existing = productDescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Description not found with id: " + id));

        ProductMaster product = productMasterRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));

        existing.setProduct(product);
        existing.setDescriptions(dto.getDescriptions());

        ProductDescription saved = productDescriptionRepository.save(existing);
        return productDescriptionMapper.toDto(saved);
    }

    public ProductDescriptionDTOResponse patchProductDescription(Long id, ProductDescriptionDTO dto) {
        ProductDescription existing = productDescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Description not found with id: " + id));

        if (dto.getProductId() != null) {
            ProductMaster product = productMasterRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));
            existing.setProduct(product);
        }

        if (dto.getDescriptions() != null)
            existing.setDescriptions(dto.getDescriptions());

        ProductDescription saved = productDescriptionRepository.save(existing);
        return productDescriptionMapper.toDto(saved);
    }

    public void deleteProductDescription(Long id) {
        ProductDescription existing = productDescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Description not found with id: " + id));
        productDescriptionRepository.delete(existing);
    }
}
