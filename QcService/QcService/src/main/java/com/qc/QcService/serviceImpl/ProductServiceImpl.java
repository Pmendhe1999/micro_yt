package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.ProductDTO;
import com.qc.QcService.entities.Product;
import com.qc.QcService.entities.QualitativeCheck;
import com.qc.QcService.entities.QuantitativeCheck;
import com.qc.QcService.repositories.ProductRepository;
import com.qc.QcService.repositories.QualitativeCheckRepository;
import com.qc.QcService.repositories.QuantitativeCheckRepository;
import com.qc.QcService.services.IdentityClient;
import com.qc.QcService.services.ProductService;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QualitativeCheckRepository qualitativeCheckRepository;

    @Autowired
    private QuantitativeCheckRepository quantitativeCheckRepository;

    @Autowired
    private IdentityClient identityClient;

    @Override
    public Product saveProduct(ProductDTO dto, String token) {
        try {
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            Product product = new Product();
            product.setBatchNo(dto.getBatchNo());
            product.setExpDate(dto.getExpDate());
            product.setHsnCode(dto.getHsnCode());
            product.setInStockQuantity(dto.getInStockQuantity());
            product.setIsPublished(dto.getIsPublished());
            product.setMfgDate(dto.getMfgDate());
            product.setName(dto.getName());
            product.setOrderNo(dto.getOrderNo());
            product.setPrice(dto.getPrice());
            product.setProductCode(dto.getProductCode());
            product.setSerialNo(dto.getSerialNo());
            product.setStatus(dto.getStatus());
            product.setUnit(dto.getUnit());

            if (dto.getQualitativeCheckId() != null) {
                QualitativeCheck qc = qualitativeCheckRepository.findById(dto.getQualitativeCheckId())
                        .orElseThrow(() -> new NoSuchElementException("QualitativeCheck not found with id " + dto.getQualitativeCheckId()));
                product.setQualitativeCheck(qc);
            }

            if (dto.getQuantitativeCheckId() != null) {
                QuantitativeCheck qtc = quantitativeCheckRepository.findById(dto.getQuantitativeCheckId())
                        .orElseThrow(() -> new NoSuchElementException("QuantitativeCheck not found with id " + dto.getQuantitativeCheckId()));
                product.setQuantitativeCheck(qtc);
            }

            product.setCreatedDate(LocalDateTime.now());
            product.setLastModifiedDate(LocalDateTime.now());

            Product saved = productRepository.save(product);
            log.info("✅ Product saved with id={} by {} (role={})", saved.getId(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("❌ Error saving Product: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving Product: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Product> getAllProducts(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return productRepository.findByNameContainingIgnoreCase(search, pageable);
            }
            return productRepository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching Products: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching Products: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        try {
            return productRepository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching Product id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching Product: " + e.getMessage(), e);
        }
    }

    @Override
    public Product updateProduct(Long id, ProductDTO dto, String token) {
        try {
            Product existing = productRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            existing.setBatchNo(dto.getBatchNo());
            existing.setExpDate(dto.getExpDate());
            existing.setHsnCode(dto.getHsnCode());
            existing.setInStockQuantity(dto.getInStockQuantity());
            existing.setIsPublished(dto.getIsPublished());
            existing.setMfgDate(dto.getMfgDate());
            existing.setName(dto.getName());
            existing.setOrderNo(dto.getOrderNo());
            existing.setPrice(dto.getPrice());
            existing.setProductCode(dto.getProductCode());
            existing.setSerialNo(dto.getSerialNo());
            existing.setStatus(dto.getStatus());
            existing.setUnit(dto.getUnit());

            if (dto.getQualitativeCheckId() != null) {
                QualitativeCheck qc = qualitativeCheckRepository.findById(dto.getQualitativeCheckId())
                        .orElseThrow(() -> new NoSuchElementException("QualitativeCheck not found with id " + dto.getQualitativeCheckId()));
                existing.setQualitativeCheck(qc);
            }

            if (dto.getQuantitativeCheckId() != null) {
                QuantitativeCheck qtc = quantitativeCheckRepository.findById(dto.getQuantitativeCheckId())
                        .orElseThrow(() -> new NoSuchElementException("QuantitativeCheck not found with id " + dto.getQuantitativeCheckId()));
                existing.setQuantitativeCheck(qtc);
            }

            existing.setLastModifiedDate(LocalDateTime.now());

            productRepository.save(existing);
            log.info("✅ Product id={} updated by {} (role={})", id, modifiedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error updating Product: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating Product: " + e.getMessage(), e);
        }
    }

    @Override
    public Product deleteProduct(Long id, String token) {
        try {
            Product existing = productRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            productRepository.delete(existing);
            log.info("🗑️ Product id={} deleted by {} (role={})", id, deletedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error deleting Product: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting Product: " + e.getMessage(), e);
        }
    }
}
