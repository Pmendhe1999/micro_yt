package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.DeliveryItemsMasterDTO;
import com.qc.QcService.entities.DeliveryChallanMaster;
import com.qc.QcService.entities.DeliveryItemsMaster;
import com.qc.QcService.repositories.DeliveryChallanMasterRepository;
import com.qc.QcService.repositories.DeliveryItemsMasterRepository;
import com.qc.QcService.services.DeliveryItemsMasterService;
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
public class DeliveryItemsMasterServiceImpl implements DeliveryItemsMasterService {
    @Autowired
    private DeliveryItemsMasterRepository repository;

    @Autowired
    private DeliveryChallanMasterRepository challanRepository;

    @Autowired
    private IdentityClient identityClient; // for token validation

    @Override
    public DeliveryItemsMaster saveItem(DeliveryItemsMasterDTO dto, String token) {
        try {
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            DeliveryChallanMaster challan = challanRepository.findById(dto.getChallanId())
                    .orElseThrow(() -> new NoSuchElementException("Challan not found with id " + dto.getChallanId()));

            DeliveryItemsMaster item = new DeliveryItemsMaster();
            item.setBatchNo(dto.getBatchNo());
            item.setDescription(dto.getDescription());
            item.setExpDate(dto.getExpDate());
            item.setHsnCode(dto.getHsnCode());
            item.setMfgDate(dto.getMfgDate());
            item.setName(dto.getName());
            item.setOrderNo(dto.getOrderNo());
            item.setProductCode(dto.getProductCode());
            item.setQuantity(dto.getQuantity());
            item.setSerialNo(dto.getSerialNo());
            item.setUnit(dto.getUnit());
            item.setChallan(challan);
            item.setCreatedDate(LocalDateTime.now());
            item.setLastModifiedDate(LocalDateTime.now());

            DeliveryItemsMaster saved = repository.save(item);
            log.info("✅ DeliveryItemsMaster saved with id={} by {} (role={})", saved.getId(), createdBy, role);
            return saved;
        } catch (Exception e) {
            log.error("❌ Error saving DeliveryItemsMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving DeliveryItemsMaster: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<DeliveryItemsMaster> getAllItems(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching DeliveryItemsMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching DeliveryItemsMaster: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<DeliveryItemsMaster> getItemById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching DeliveryItemsMaster id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching DeliveryItemsMaster: " + e.getMessage(), e);
        }
    }

    @Override
    public DeliveryItemsMaster updateItem(Long id, DeliveryItemsMasterDTO dto, String token) {
        try {
            DeliveryItemsMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("DeliveryItemsMaster not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            DeliveryChallanMaster challan = challanRepository.findById(dto.getChallanId())
                    .orElseThrow(() -> new NoSuchElementException("Challan not found with id " + dto.getChallanId()));

            existing.setBatchNo(dto.getBatchNo());
            existing.setDescription(dto.getDescription());
            existing.setExpDate(dto.getExpDate());
            existing.setHsnCode(dto.getHsnCode());
            existing.setMfgDate(dto.getMfgDate());
            existing.setName(dto.getName());
            existing.setOrderNo(dto.getOrderNo());
            existing.setProductCode(dto.getProductCode());
            existing.setQuantity(dto.getQuantity());
            existing.setSerialNo(dto.getSerialNo());
            existing.setUnit(dto.getUnit());
            existing.setChallan(challan);
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("✅ DeliveryItemsMaster id={} updated by {} (role={})", id, modifiedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error updating DeliveryItemsMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating DeliveryItemsMaster: " + e.getMessage(), e);
        }
    }

    @Override
    public DeliveryItemsMaster deleteItem(Long id, String token) {
        try {
            DeliveryItemsMaster existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("DeliveryItemsMaster not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("🗑️ DeliveryItemsMaster id={} deleted by {} (role={})", id, deletedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error deleting DeliveryItemsMaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting DeliveryItemsMaster: " + e.getMessage(), e);
        }
    }
}
