package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.ProductMasterDTO;
import com.qc.QcService.entities.ProductMaster;
import com.qc.QcService.repositories.ProductMasterRepository;
import com.qc.QcService.services.IdentityClient;
import com.qc.QcService.services.ProductMasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductMasterServiceImpl implements ProductMasterService {


    @Autowired
    private ProductMasterRepository repository;

//    private JwtService jwtService;
    @Autowired
    private IdentityClient identityClient;

    @Override
    public ProductMaster saveProduct(ProductMasterDTO dto, String token) {
        try {
            if (repository.existsByName(dto.getName())) {
                log.warn("Duplicate product creation attempt: {}", dto.getName());
                throw new IllegalArgumentException("Product already exists");
            }
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            ProductMaster product = new ProductMaster();
            product.setName(dto.getName());
            product.setProductCode(dto.getProductCode());
            product.setSerialNo(dto.getSerialNo());
            product.setOrderNo(dto.getOrderNo());
            product.setHsnCode(dto.getHsnCode());
            product.setUnit(dto.getUnit());
            product.setPrice(dto.getPrice());
            product.setQuantity(dto.getQuantity());
            product.setInStockQuantity(dto.getInStockQuantity());
            product.setMfgDate(dto.getMfgDate());
            product.setExpDate(dto.getExpDate());
            product.setIsPublished(dto.getIsPublished());
            product.setStatus(dto.getStatus());
            product.setCreatedDate(LocalDateTime.now());
            product.setLastModifiedDate(LocalDateTime.now());

            ProductMaster saved = repository.save(product);
            log.info("Product '{}' created by user={} role={}", saved.getName(), createdBy, role);
            return saved;
        } catch (Exception e) {
            log.error("Error while saving Product: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving Product: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<ProductMaster> getAllProducts(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching products: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching products: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ProductMaster> getProductById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching Product by id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching Product: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductMaster updateProduct(Long id, ProductMasterDTO dto, String token) {
        try {
            ProductMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

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
            existing.setIsPublished(dto.getIsPublished());
            existing.setStatus(dto.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("Product id={} updated by user={} role={}", id, modifiedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error updating Product: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating Product: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductMaster deleteProduct(Long id, String token) {
        try {
            ProductMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("Product id={} deleted by user={} role={}", id, deletedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error deleting Product: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting Product: " + e.getMessage(), e);
        }
    }
}
