package com.quiz.services;

import com.quiz.dto.DeliveryItemsMasterDTO;
import com.quiz.dto.DeliveryItemsMasterDTOResponse;
import com.quiz.entities.DeliveryChallanMaster;
import com.quiz.entities.DeliveryItemsMaster;
import com.quiz.exception.ResourceNotFoundException;
import com.quiz.mapper.DeliveryItemsMasterMapper;
import com.quiz.repositories.DeliveryChallanMasterRepository;
import com.quiz.repositories.DeliveryItemsMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DeliveryItemsMasterService {
    @Autowired
    private DeliveryItemsMasterRepository deliveryItemsMasterRepository;

    @Autowired
    private DeliveryChallanMasterRepository deliveryChallanMasterRepository;

    @Autowired
    private DeliveryItemsMasterMapper mapper;

    public void createAllDeliveryItems(List<DeliveryItemsMasterDTO> dtoList) {
        List<DeliveryItemsMaster> entities = mapper.toEntityList(dtoList);
        entities.forEach(entity -> {
            DeliveryChallanMaster challan = deliveryChallanMasterRepository.findById(entity.getChallan().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Challan not found with ID: " + entity.getChallan().getId()));
            entity.setChallan(challan);
        });
        deliveryItemsMasterRepository.saveAll(entities);
    }

    public void createDeliveryItem(DeliveryItemsMasterDTO dto) {
        DeliveryItemsMaster entity = mapper.toEntity(dto);
        DeliveryChallanMaster challan = deliveryChallanMasterRepository.findById(entity.getChallan().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Challan not found with ID: " + entity.getChallan().getId()));
        entity.setChallan(challan);
        deliveryItemsMasterRepository.save(entity);
    }

    public Page<DeliveryItemsMasterDTOResponse> getAllDeliveryItemsWithFilters(
            String batchNo,
            String name,
            String productCode,
            String orderNo,
            String serialNo,
            String unit,
            String hsnCode,
            List<Long> challanIds,
            Pageable pageable
    ) {
        Page<DeliveryItemsMaster> page = deliveryItemsMasterRepository.searchDeliveryItemsAdvanced(
                batchNo, name, productCode, orderNo, serialNo, unit, hsnCode, challanIds, pageable
        );

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Delivery Items found with given filters");
        }

        List<DeliveryItemsMasterDTOResponse> dtoList = mapper.toDtoList(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public DeliveryItemsMasterDTOResponse getDeliveryItemById(Long id) {
        DeliveryItemsMaster entity = deliveryItemsMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Item not found with id: " + id));
        return mapper.toDto(entity);
    }

    public DeliveryItemsMasterDTOResponse updateDeliveryItem(Long id, DeliveryItemsMasterDTO dto) {
        DeliveryItemsMaster existing = deliveryItemsMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Item not found with id: " + id));

        DeliveryChallanMaster challan = deliveryChallanMasterRepository.findById(dto.getChallanId())
                .orElseThrow(() -> new ResourceNotFoundException("Challan not found with ID: " + dto.getChallanId()));

        existing.setChallan(challan);
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

        DeliveryItemsMaster saved = deliveryItemsMasterRepository.save(existing);
        return mapper.toDto(saved);
    }

    public DeliveryItemsMasterDTOResponse patchDeliveryItem(Long id, DeliveryItemsMasterDTO dto) {
        DeliveryItemsMaster existing = deliveryItemsMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Item not found with id: " + id));

        if (dto.getChallanId() != null) {
            DeliveryChallanMaster challan = deliveryChallanMasterRepository.findById(dto.getChallanId())
                    .orElseThrow(() -> new ResourceNotFoundException("Challan not found with ID: " + dto.getChallanId()));
            existing.setChallan(challan);
        }

        if (dto.getBatchNo() != null) existing.setBatchNo(dto.getBatchNo());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getExpDate() != null) existing.setExpDate(dto.getExpDate());
        if (dto.getHsnCode() != null) existing.setHsnCode(dto.getHsnCode());
        if (dto.getMfgDate() != null) existing.setMfgDate(dto.getMfgDate());
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getOrderNo() != null) existing.setOrderNo(dto.getOrderNo());
        if (dto.getProductCode() != null) existing.setProductCode(dto.getProductCode());
        if (dto.getQuantity() != null) existing.setQuantity(dto.getQuantity());
        if (dto.getSerialNo() != null) existing.setSerialNo(dto.getSerialNo());
        if (dto.getUnit() != null) existing.setUnit(dto.getUnit());

        DeliveryItemsMaster saved = deliveryItemsMasterRepository.save(existing);
        return mapper.toDto(saved);
    }

    public void deleteDeliveryItem(Long id) {
        DeliveryItemsMaster existing = deliveryItemsMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Item not found with id: " + id));
        deliveryItemsMasterRepository.delete(existing);
    }
}
