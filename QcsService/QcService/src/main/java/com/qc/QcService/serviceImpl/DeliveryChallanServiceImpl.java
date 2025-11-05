package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.DeliveryChallanDTO;
import com.qc.QcService.entities.DeliveryChallan;
import com.qc.QcService.repositories.DeliveryChallanRepository;
import com.qc.QcService.services.DeliveryChallanService;
import com.qc.QcService.services.IdentityClient;
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
public class DeliveryChallanServiceImpl implements DeliveryChallanService {

    @Autowired
    private DeliveryChallanRepository repository;
    @Autowired
    private IdentityClient identityClient; // existing token validation client

    @Override
    public DeliveryChallan saveChallan(DeliveryChallanDTO dto, String token) {
        try {
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            DeliveryChallan challan = new DeliveryChallan();
            challan.setName(dto.getName());
            challan.setDescriptions(dto.getDescriptions());
            challan.setStatus(dto.getStatus());
            challan.setCreatedDate(LocalDateTime.now());
            challan.setLastModifiedDate(LocalDateTime.now());

            DeliveryChallan saved = repository.save(challan);
            log.info("✅ DeliveryChallan saved with id={} by {} (role={})", saved.getId(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("❌ Error saving DeliveryChallan: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving DeliveryChallan: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<DeliveryChallan> getAllChallans(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching DeliveryChallans: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching DeliveryChallans: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<DeliveryChallan> getChallanById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching DeliveryChallan id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching DeliveryChallan: " + e.getMessage(), e);
        }
    }

    @Override
    public DeliveryChallan updateChallan(Long id, DeliveryChallanDTO dto, String token) {
        try {
            DeliveryChallan existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("DeliveryChallan not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            existing.setName(dto.getName());
            existing.setDescriptions(dto.getDescriptions());
            existing.setStatus(dto.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("✅ DeliveryChallan id={} updated by {} (role={})", id, modifiedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error updating DeliveryChallan: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating DeliveryChallan: " + e.getMessage(), e);
        }
    }

    @Override
    public DeliveryChallan deleteChallan(Long id, String token) {
        try {
            DeliveryChallan existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("DeliveryChallan not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("🗑️ DeliveryChallan id={} deleted by {} (role={})", id, deletedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error deleting DeliveryChallan: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting DeliveryChallan: " + e.getMessage(), e);
        }
    }
}
