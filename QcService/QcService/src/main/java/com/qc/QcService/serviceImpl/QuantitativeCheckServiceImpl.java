package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.QuantitativeCheckDTO;
import com.qc.QcService.entities.QuantitativeCheck;
import com.qc.QcService.entities.QuantitativeCheckMaster;
import com.qc.QcService.repositories.QuantitativeCheckMasterRepository;
import com.qc.QcService.repositories.QuantitativeCheckRepository;
import com.qc.QcService.services.IdentityClient;
import com.qc.QcService.services.QuantitativeCheckService;
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
public class QuantitativeCheckServiceImpl implements QuantitativeCheckService {
    @Autowired
    private  QuantitativeCheckRepository repository;
    @Autowired
    private  QuantitativeCheckMasterRepository masterRepository;
    @Autowired
    private  IdentityClient identityClient;

    @Override
    public QuantitativeCheck saveCheck(QuantitativeCheckDTO dto, String token) {
        try {
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            QuantitativeCheckMaster master = masterRepository.findById(dto.getQuantitativeCheckMasterId())
                    .orElseThrow(() -> new NoSuchElementException("QuantitativeCheckMaster not found with id " + dto.getQuantitativeCheckMasterId()));

            QuantitativeCheck check = new QuantitativeCheck();
            check.setDescription(dto.getDescription());
            check.setIsScan(dto.getIsScan());
            check.setStatus(dto.getStatus());
            check.setValue(dto.getValue());
            check.setQuantitativeCheckMaster(master);
            check.setCreatedDate(LocalDateTime.now());
            check.setLastModifiedDate(LocalDateTime.now());

            QuantitativeCheck saved = repository.save(check);
            log.info("✅ QuantitativeCheck saved with id={} by {} (role={})", saved.getId(), createdBy, role);
            return saved;

        } catch (Exception e) {
            log.error("❌ Error saving QuantitativeCheck: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving QuantitativeCheck: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<QuantitativeCheck> getAllChecks(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByValueContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching QuantitativeChecks: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching QuantitativeChecks: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<QuantitativeCheck> getCheckById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching QuantitativeCheck id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching QuantitativeCheck: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantitativeCheck updateCheck(Long id, QuantitativeCheckDTO dto, String token) {
        try {
            QuantitativeCheck existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("QuantitativeCheck not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            QuantitativeCheckMaster master = masterRepository.findById(dto.getQuantitativeCheckMasterId())
                    .orElseThrow(() -> new NoSuchElementException("QuantitativeCheckMaster not found with id " + dto.getQuantitativeCheckMasterId()));

            existing.setDescription(dto.getDescription());
            existing.setIsScan(dto.getIsScan());
            existing.setStatus(dto.getStatus());
            existing.setValue(dto.getValue());
            existing.setQuantitativeCheckMaster(master);
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("✅ QuantitativeCheck id={} updated by {} (role={})", id, modifiedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error updating QuantitativeCheck: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating QuantitativeCheck: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantitativeCheck deleteCheck(Long id, String token) {
        try {
            QuantitativeCheck existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("QuantitativeCheck not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("🗑️ QuantitativeCheck id={} deleted by {} (role={})", id, deletedBy, role);
            return existing;

        } catch (Exception e) {
            log.error("Error deleting QuantitativeCheck: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting QuantitativeCheck: " + e.getMessage(), e);
        }
    }
}
