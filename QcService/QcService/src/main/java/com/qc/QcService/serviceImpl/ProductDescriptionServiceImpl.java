package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.ProductDescriptionDTO;
import com.qc.QcService.entities.Product;
import com.qc.QcService.entities.ProductDescription;
import com.qc.QcService.repositories.ProductDescriptionRepository;
import com.qc.QcService.repositories.ProductRepository;
import com.qc.QcService.services.IdentityClient;
import com.qc.QcService.services.ProductDescriptionService;
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
public class ProductDescriptionServiceImpl implements ProductDescriptionService {

    @Autowired
    private ProductDescriptionRepository repository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private IdentityClient identityClient; // your existing token validation client

    @Override
    public ProductDescription saveDescription(ProductDescriptionDTO dto, String token) {
        try {
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new NoSuchElementException("Product not found with id " + dto.getProductId()));

            ProductDescription description = new ProductDescription();
            description.setProduct(product);
            description.setDescriptions(dto.getDescriptions());
            description.setCreatedDate(LocalDateTime.now());
            description.setLastModifiedDate(LocalDateTime.now());

            ProductDescription saved = repository.save(description);
            log.info("✅ ProductDescription saved with id={} by {} (role={})", saved.getId(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("❌ Error saving ProductDescription: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving ProductDescription: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<ProductDescription> getAllDescriptions(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByDescriptionsContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching ProductDescriptions: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching ProductDescriptions: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ProductDescription> getDescriptionById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching ProductDescription id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching ProductDescription: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductDescription updateDescription(Long id, ProductDescriptionDTO dto, String token) {
        try {
            ProductDescription existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("ProductDescription not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new NoSuchElementException("Product not found with id " + dto.getProductId()));

            existing.setProduct(product);
            existing.setDescriptions(dto.getDescriptions());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("✅ ProductDescription id={} updated by {} (role={})", id, modifiedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error updating ProductDescription: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating ProductDescription: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductDescription deleteDescription(Long id, String token) {
        try {
            ProductDescription existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("ProductDescription not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("🗑️ ProductDescription id={} deleted by {} (role={})", id, deletedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error deleting ProductDescription: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting ProductDescription: " + e.getMessage(), e);
        }
    }

}
