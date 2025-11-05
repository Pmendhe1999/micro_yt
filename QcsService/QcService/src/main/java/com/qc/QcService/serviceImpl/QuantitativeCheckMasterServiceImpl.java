package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.QuantitativeCheckMasterDTO;
import com.qc.QcService.entities.LabelScanMaster;
import com.qc.QcService.entities.QuantitativeCheckMaster;
import com.qc.QcService.repositories.LabelScanMasterRepository;
import com.qc.QcService.repositories.QuantitativeCheckMasterRepository;
import com.qc.QcService.services.IdentityClient;
import com.qc.QcService.services.QuantitativeCheckMasterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuantitativeCheckMasterServiceImpl implements QuantitativeCheckMasterService {
    private static final Logger log = LoggerFactory.getLogger(QuantitativeCheckMasterServiceImpl.class);

    private final QuantitativeCheckMasterRepository repository;
    private final LabelScanMasterRepository scanRepository;
    private final IdentityClient identityClient;

    @Override
    public QuantitativeCheckMaster saveCheck(QuantitativeCheckMasterDTO dto, String token) {
        try {
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            LabelScanMaster scanMaster = scanRepository.findById(dto.getScanMasterId())
                    .orElseThrow(() -> new NoSuchElementException("Scan Master not found with id " + dto.getScanMasterId()));

            QuantitativeCheckMaster check = new QuantitativeCheckMaster();
            check.setName(dto.getName());
            check.setDescription(dto.getDescription());
            check.setCheckStatus(QuantitativeCheckMaster.CheckStatus.valueOf(dto.getCheckStatus().toUpperCase()));
            check.setStatus(dto.getStatus());
            check.setScanMaster(scanMaster);
            check.setCreatedDate(LocalDateTime.now());
            check.setLastModifiedDate(LocalDateTime.now());

            QuantitativeCheckMaster saved = repository.save(check);
            log.info("✅ QuantitativeCheck saved with id={} by {} (role={})", saved.getId(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("❌ Error saving QuantitativeCheckMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving check: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<QuantitativeCheckMaster> getAllChecks(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching checks: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching checks: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<QuantitativeCheckMaster> getCheckById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching QuantitativeCheck by id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching check: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantitativeCheckMaster updateCheck(Long id, QuantitativeCheckMasterDTO dto, String token) {
        try {
            QuantitativeCheckMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("QuantitativeCheck not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            LabelScanMaster scanMaster = scanRepository.findById(dto.getScanMasterId())
                    .orElseThrow(() -> new NoSuchElementException("Scan Master not found with id " + dto.getScanMasterId()));

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setCheckStatus(QuantitativeCheckMaster.CheckStatus.valueOf(dto.getCheckStatus().toUpperCase()));
            existing.setStatus(dto.getStatus());
            existing.setScanMaster(scanMaster);
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("✅ QuantitativeCheck id={} updated by {} (role={})", id, modifiedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error updating QuantitativeCheckMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating check: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantitativeCheckMaster deleteCheck(Long id, String token) {
        try {
            QuantitativeCheckMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("QuantitativeCheck not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("🗑️ QuantitativeCheck id={} deleted by {} (role={})", id, deletedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error deleting QuantitativeCheckMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting check: " + e.getMessage(), e);
        }
    }
}
