package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.QualitativeCheckDTO;
import com.qc.QcService.entities.QualitativeCheck;
import com.qc.QcService.entities.QualitativeCheckMaster;
import com.qc.QcService.repositories.QualitativeCheckMasterRepository;
import com.qc.QcService.repositories.QualitativeCheckRepository;
import com.qc.QcService.services.IdentityClient;
import com.qc.QcService.services.QualitativeCheckService;
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
public class QualitativeCheckServiceImpl  implements QualitativeCheckService {

    @Autowired
    private QualitativeCheckRepository repository;

    @Autowired
    private QualitativeCheckMasterRepository masterRepository;

    @Autowired
    private IdentityClient identityClient;

    @Override
    public QualitativeCheck saveCheck(QualitativeCheckDTO dto, String token) {
        try {
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            QualitativeCheckMaster master = masterRepository.findById(dto.getQualitativeCheckMasterId())
                    .orElseThrow(() -> new NoSuchElementException("QualitativeCheckMaster not found with id " + dto.getQualitativeCheckMasterId()));

            QualitativeCheck check = new QualitativeCheck();
            check.setDescription(dto.getDescription());
            check.setScan(dto.getScan());
            check.setStatus(dto.getStatus());
            check.setValue(dto.getValue());
            check.setQualitativeCheckMaster(master);
            check.setCreatedDate(LocalDateTime.now());
            check.setLastModifiedDate(LocalDateTime.now());

            QualitativeCheck saved = repository.save(check);
            log.info("✅ QualitativeCheck saved with id={} by {} (role={})", saved.getId(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("❌ Error saving QualitativeCheck: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving QualitativeCheck: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<QualitativeCheck> getAllChecks(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByValueContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching QualitativeChecks: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching QualitativeChecks: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<QualitativeCheck> getCheckById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching QualitativeCheck id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching QualitativeCheck: " + e.getMessage(), e);
        }
    }

    @Override
    public QualitativeCheck updateCheck(Long id, QualitativeCheckDTO dto, String token) {
        try {
            QualitativeCheck existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("QualitativeCheck not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            QualitativeCheckMaster master = masterRepository.findById(dto.getQualitativeCheckMasterId())
                    .orElseThrow(() -> new NoSuchElementException("QualitativeCheckMaster not found with id " + dto.getQualitativeCheckMasterId()));

            existing.setDescription(dto.getDescription());
            existing.setIsScan(dto.getScan());
            existing.setStatus(dto.getStatus());
            existing.setValue(dto.getValue());
            existing.setQualitativeCheckMaster(master);
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("✅ QualitativeCheck id={} updated by {} (role={})", id, modifiedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error updating QualitativeCheck: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating QualitativeCheck: " + e.getMessage(), e);
        }
    }

    @Override
    public QualitativeCheck deleteCheck(Long id, String token) {
        try {
            QualitativeCheck existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("QualitativeCheck not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("🗑️ QualitativeCheck id={} deleted by {} (role={})", id, deletedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error deleting QualitativeCheck: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting QualitativeCheck: " + e.getMessage(), e);
        }
    }
}
