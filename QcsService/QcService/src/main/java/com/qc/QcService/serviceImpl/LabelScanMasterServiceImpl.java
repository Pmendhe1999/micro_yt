package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.LabelScanMasterDTO;
import com.qc.QcService.entities.LabelScanMaster;
import com.qc.QcService.repositories.LabelScanMasterRepository;
import com.qc.QcService.services.IdentityClient;
import com.qc.QcService.services.LabelScanMasterService;
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
public class LabelScanMasterServiceImpl implements LabelScanMasterService {

    @Autowired
    private LabelScanMasterRepository repository;

    @Autowired
    private IdentityClient identityClient;

    @Override
    public LabelScanMaster saveLabel(LabelScanMasterDTO dto, String token) {
        try {
            if (repository.existsByName(dto.getName())) {
                throw new IllegalArgumentException("Label Scan already exists");
            }

            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            LabelScanMaster label = new LabelScanMaster();
            label.setName(dto.getName());
            label.setDescription(dto.getDescription());
            label.setScanType(dto.getScanType());
            label.setSeqNumber(dto.getSeqNumber());
            label.setCheckStatus(LabelScanMaster.CheckStatus.valueOf(dto.getCheckStatus().toUpperCase()));
            label.setStatus(dto.getStatus());
            label.setCreatedDate(LocalDateTime.now());
            label.setLastModifiedDate(LocalDateTime.now());

            LabelScanMaster saved = repository.save(label);
            log.info("Label '{}' created by user={} role={}", saved.getName(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("Error while saving Label: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving Label: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<LabelScanMaster> getAllLabels(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching labels: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching labels: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<LabelScanMaster> getLabelById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching Label by id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching Label: " + e.getMessage(), e);
        }
    }

    @Override
    public LabelScanMaster updateLabel(Long id, LabelScanMasterDTO dto, String token) {
        try {
            LabelScanMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Label not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setScanType(dto.getScanType());
            existing.setSeqNumber(dto.getSeqNumber());
            existing.setCheckStatus(LabelScanMaster.CheckStatus.valueOf(dto.getCheckStatus().toUpperCase()));
            existing.setStatus(dto.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("Label id={} updated by user={} role={}", id, modifiedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error updating Label: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating Label: " + e.getMessage(), e);
        }
    }

    @Override
    public LabelScanMaster deleteLabel(Long id, String token) {
        try {
            LabelScanMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Label not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("Label id={} deleted by user={} role={}", id, deletedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error deleting Label: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting Label: " + e.getMessage(), e);
        }
    }
}
