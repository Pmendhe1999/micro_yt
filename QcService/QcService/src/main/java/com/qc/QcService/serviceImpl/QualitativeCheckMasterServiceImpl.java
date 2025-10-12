package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.QualitativeCheckMasterDTO;
import com.qc.QcService.entities.LabelScanMaster;
import com.qc.QcService.entities.QualitativeCheckMaster;
import com.qc.QcService.repositories.LabelScanMasterRepository;
import com.qc.QcService.repositories.QualitativeCheckMasterRepository;
import com.qc.QcService.services.IdentityClient;
import com.qc.QcService.services.QualitativeCheckMasterService;
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
public class QualitativeCheckMasterServiceImpl implements QualitativeCheckMasterService {


    @Autowired
    private  QualitativeCheckMasterRepository repository;
    @Autowired
    private  LabelScanMasterRepository scanRepository;
    @Autowired
    private  IdentityClient identityClient;

    @Override
    public QualitativeCheckMaster saveCheck(QualitativeCheckMasterDTO dto, String token) {
        try {
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            LabelScanMaster scanMaster = scanRepository.findById(dto.getScanMasterId())
                    .orElseThrow(() -> new NoSuchElementException("Scan Master not found with id " + dto.getScanMasterId()));

            QualitativeCheckMaster check = new QualitativeCheckMaster();
            check.setName(dto.getName());
            check.setDescription(dto.getDescription());
            check.setCheckStatus(QualitativeCheckMaster.CheckStatus.valueOf(dto.getCheckStatus().toUpperCase()));
            check.setStatus(dto.getStatus());
            check.setScanMaster(scanMaster);
            check.setCreatedDate(LocalDateTime.now());
            check.setLastModifiedDate(LocalDateTime.now());

            QualitativeCheckMaster saved = repository.save(check);
            log.info("✅ QualitativeCheck saved with id={} by {} (role={})", saved.getId(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("❌ Error saving QualitativeCheckMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving check: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<QualitativeCheckMaster> getAllChecks(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching QualitativeChecks: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching checks: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<QualitativeCheckMaster> getCheckById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching QualitativeCheck by id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching check: " + e.getMessage(), e);
        }
    }

    @Override
    public QualitativeCheckMaster updateCheck(Long id, QualitativeCheckMasterDTO dto, String token) {
        try {
            QualitativeCheckMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("QualitativeCheck not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            LabelScanMaster scanMaster = scanRepository.findById(dto.getScanMasterId())
                    .orElseThrow(() -> new NoSuchElementException("Scan Master not found with id " + dto.getScanMasterId()));

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setCheckStatus(QualitativeCheckMaster.CheckStatus.valueOf(dto.getCheckStatus().toUpperCase()));
            existing.setStatus(dto.getStatus());
            existing.setScanMaster(scanMaster);
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("✅ QualitativeCheck id={} updated by {} (role={})", id, modifiedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error updating QualitativeCheckMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating check: " + e.getMessage(), e);
        }
    }

    @Override
    public QualitativeCheckMaster deleteCheck(Long id, String token) {
        try {
            QualitativeCheckMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("QualitativeCheck not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("🗑️ QualitativeCheck id={} deleted by {} (role={})", id, deletedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error deleting QualitativeCheckMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting check: " + e.getMessage(), e);
        }
    }
}
