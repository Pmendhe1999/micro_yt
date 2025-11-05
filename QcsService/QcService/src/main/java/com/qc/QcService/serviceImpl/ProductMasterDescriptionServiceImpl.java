package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.ProductMasterDescriptionDTO;
import com.qc.QcService.entities.ProductMaster;
import com.qc.QcService.entities.ProductMasterDescription;
import com.qc.QcService.repositories.ProductMasterDescriptionRepository;
import com.qc.QcService.repositories.ProductMasterRepository;
import com.qc.QcService.services.IdentityClient;
import com.qc.QcService.services.ProductMasterDescriptionService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductMasterDescriptionServiceImpl implements ProductMasterDescriptionService {

    private static final Logger log = LoggerFactory.getLogger(ProductMasterDescriptionServiceImpl.class);

    @Autowired
    private ProductMasterDescriptionRepository repository;

    @Autowired
    private ProductMasterRepository productRepository;

    @Autowired
    private IdentityClient identityClient;

    @Override
    public ProductMasterDescription saveDescription(ProductMasterDescriptionDTO dto, String token) {
        try {
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            ProductMaster product = productRepository.findById(dto.getProductMasterId())
                    .orElseThrow(() -> new NoSuchElementException("Product not found with id " + dto.getProductMasterId()));

            ProductMasterDescription desc = new ProductMasterDescription();
            desc.setProductMaster(product);
            desc.setDescriptions(dto.getDescriptions());

            ProductMasterDescription saved = repository.save(desc);
            log.info("Description saved for Product id={} by user={} role={}", product.getId(), createdBy, role);
            return saved;
        } catch (Exception e) {
            log.error("Error saving ProductMasterDescription: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving description: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<ProductMasterDescription> getAllDescriptions(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByDescriptionsContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching descriptions: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching descriptions: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ProductMasterDescription> getDescriptionById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching description by id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching description: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductMasterDescription updateDescription(Long id, ProductMasterDescriptionDTO dto, String token) {
        try {
            ProductMasterDescription existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Description not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            ProductMaster product = productRepository.findById(dto.getProductMasterId())
                    .orElseThrow(() -> new NoSuchElementException("Product not found with id " + dto.getProductMasterId()));

            existing.setProductMaster(product);
            existing.setDescriptions(dto.getDescriptions());

            repository.save(existing);
            log.info("Description id={} updated by user={} role={}", id, modifiedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error updating ProductMasterDescription: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating description: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductMasterDescription deleteDescription(Long id, String token) {
        try {
            ProductMasterDescription existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Description not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("Description id={} deleted by user={} role={}", id, deletedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error deleting ProductMasterDescription: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting description: " + e.getMessage(), e);
        }
    }
}
