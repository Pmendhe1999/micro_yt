package com.qc.QcService.serviceImpl;

import com.qc.QcService.dto.DeliveryItemsDTO;
import com.qc.QcService.entities.DeliveryChallan;
import com.qc.QcService.entities.DeliveryItems;
import com.qc.QcService.repositories.DeliveryChallanRepository;
import com.qc.QcService.repositories.DeliveryItemsRepository;
import com.qc.QcService.services.DeliveryItemsService;
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
public class DeliveryItemsServiceImpl implements DeliveryItemsService {


    @Autowired
    private DeliveryItemsRepository repository;

    @Autowired
    private DeliveryChallanRepository challanRepository;

    @Autowired
    private IdentityClient identityClient;

    @Override
    public DeliveryItems saveItem(DeliveryItemsDTO dto, String token) {
        try {
            Map<String, Object> authData = identityClient.validateToken(token);
            String createdBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            DeliveryChallan challan = challanRepository.findById(dto.getChallenId())
                    .orElseThrow(() -> new NoSuchElementException("DeliveryChallan not found with id " + dto.getChallenId()));

            DeliveryItems item = new DeliveryItems();
            item.setBatchNo(dto.getBatchNo());
            item.setDescription(dto.getDescription());
            item.setExpDate(dto.getExpDate());
            item.setHsnCode(dto.getHsnCode());
            item.setMfgDate(dto.getMfgDate());
            item.setName(dto.getName());
            item.setOrderNo(dto.getOrderNo());
            item.setProductCode(dto.getProductCode());
            item.setQualitativeCheckFailedQty(dto.getQualitativeCheckFailedQty());
            item.setQualitativeCheckPassedQty(dto.getQualitativeCheckPassedQty());
            item.setQuantitativeCheckFailedQty(dto.getQuantitativeCheckFailedQty());
            item.setQuantitativeCheckPassedQty(dto.getQuantitativeCheckPassedQty());
            item.setQuantity(dto.getQuantity());
            item.setSerialNo(dto.getSerialNo());
            item.setUnit(dto.getUnit());
            item.setDeliveryChallan(challan);
            item.setCreatedDate(LocalDateTime.now());
            item.setLastModifiedDate(LocalDateTime.now());

            DeliveryItems saved = repository.save(item);
            log.info("✅ DeliveryItem saved with id={} by {} (role={})", saved.getId(), createdBy, role);
            return saved;
        } catch (Exception e) {
            log.error("❌ Error saving DeliveryItem: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving DeliveryItem: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<DeliveryItems> getAllItems(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching DeliveryItems: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching DeliveryItems: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<DeliveryItems> getItemById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching DeliveryItem id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error fetching DeliveryItem: " + e.getMessage(), e);
        }
    }

    @Override
    public DeliveryItems updateItem(Long id, DeliveryItemsDTO dto, String token) {
        try {
            DeliveryItems existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("DeliveryItem not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String modifiedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            DeliveryChallan challan = challanRepository.findById(dto.getChallenId())
                    .orElseThrow(() -> new NoSuchElementException("DeliveryChallan not found with id " + dto.getChallenId()));

            existing.setBatchNo(dto.getBatchNo());
            existing.setDescription(dto.getDescription());
            existing.setExpDate(dto.getExpDate());
            existing.setHsnCode(dto.getHsnCode());
            existing.setMfgDate(dto.getMfgDate());
            existing.setName(dto.getName());
            existing.setOrderNo(dto.getOrderNo());
            existing.setProductCode(dto.getProductCode());
            existing.setQualitativeCheckFailedQty(dto.getQualitativeCheckFailedQty());
            existing.setQualitativeCheckPassedQty(dto.getQualitativeCheckPassedQty());
            existing.setQuantitativeCheckFailedQty(dto.getQuantitativeCheckFailedQty());
            existing.setQuantitativeCheckPassedQty(dto.getQuantitativeCheckPassedQty());
            existing.setQuantity(dto.getQuantity());
            existing.setSerialNo(dto.getSerialNo());
            existing.setUnit(dto.getUnit());
            existing.setDeliveryChallan(challan);
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("✅ DeliveryItem id={} updated by {} (role={})", id, modifiedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error updating DeliveryItem: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating DeliveryItem: " + e.getMessage(), e);
        }
    }

    @Override
    public DeliveryItems deleteItem(Long id, String token) {
        try {
            DeliveryItems existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("DeliveryItem not found with id " + id));

            Map<String, Object> authData = identityClient.validateToken(token);
            String deletedBy = (String) authData.get("username");
            String role = (String) authData.get("role");

            repository.delete(existing);
            log.info("🗑️ DeliveryItem id={} deleted by {} (role={})", id, deletedBy, role);
            return existing;
        } catch (Exception e) {
            log.error("Error deleting DeliveryItem: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting DeliveryItem: " + e.getMessage(), e);
        }
    }
}
