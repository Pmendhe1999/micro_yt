package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.DeliveryChallanMasterDTO;
import com.qc.QcService.entities.DeliveryChallanMaster;
import com.qc.QcService.repositories.DeliveryChallanMasterRepository;
import com.qc.QcService.services.DeliveryChallanMasterService;
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
public class DeliveryChallanMasterServiceImpl implements DeliveryChallanMasterService {

    @Autowired
    private DeliveryChallanMasterRepository repository;

    @Autowired
    private IdentityClient identityClient; // token validation

    @Override
    public DeliveryChallanMaster saveMaster(DeliveryChallanMasterDTO dto, String token) {
        try {
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            DeliveryChallanMaster master = new DeliveryChallanMaster();
            master.setName(dto.getName());
            master.setDescriptions(dto.getDescriptions());
            master.setStatus(dto.getStatus());
            master.setCreatedDate(LocalDateTime.now());
            master.setLastModifiedDate(LocalDateTime.now());

            DeliveryChallanMaster saved = repository.save(master);
            log.info("✅ DeliveryChallanMaster saved with id={} by {} (role={})", saved.getId(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("❌ Error saving DeliveryChallanMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving DeliveryChallanMaster: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<DeliveryChallanMaster> getAllMasters(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching DeliveryChallanMasters: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching DeliveryChallanMasters: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<DeliveryChallanMaster> getMasterById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching DeliveryChallanMaster id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching DeliveryChallanMaster: " + e.getMessage(), e);
        }
    }

    @Override
    public DeliveryChallanMaster updateMaster(Long id, DeliveryChallanMasterDTO dto, String token) {
        try {
            DeliveryChallanMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("DeliveryChallanMaster not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            existing.setName(dto.getName());
            existing.setDescriptions(dto.getDescriptions());
            existing.setStatus(dto.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("✅ DeliveryChallanMaster id={} updated by {} (role={})", id, modifiedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error updating DeliveryChallanMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating DeliveryChallanMaster: " + e.getMessage(), e);
        }
    }

    @Override
    public DeliveryChallanMaster deleteMaster(Long id, String token) {
        try {
            DeliveryChallanMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("DeliveryChallanMaster not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("🗑️ DeliveryChallanMaster id={} deleted by {} (role={})", id, deletedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error deleting DeliveryChallanMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting DeliveryChallanMaster: " + e.getMessage(), e);
        }
    }

}
