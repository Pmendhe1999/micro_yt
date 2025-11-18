package com.qc.services;

import com.qc.dto.DeliveryItemsDTO;
import com.qc.dto.DeliveryItemsDTOResponse;
import com.qc.entities.DeliveryChallan;
import com.qc.entities.DeliveryItems;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.DeliveryItemsMapper;
import com.qc.repositories.DeliveryChallanRepository;
import com.qc.repositories.DeliveryItemsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DeliveryItemsService {
    @Autowired
    private DeliveryItemsRepository deliveryItemsRepository;

    @Autowired
    private DeliveryChallanRepository deliveryChallanRepository;

    @Autowired
    private DeliveryItemsMapper mapper;

    public void createAll(List<DeliveryItemsDTO> dtoList) {
        List<DeliveryItems> entities = mapper.toEntityList(dtoList);
        entities.forEach(entity -> {
            DeliveryChallan challan = deliveryChallanRepository.findById(entity.getChallan().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Challan not found with ID: " + entity.getChallan().getId()));
            entity.setChallan(challan);
        });
        deliveryItemsRepository.saveAll(entities);
    }

    public DeliveryItemsDTOResponse create(DeliveryItemsDTO dto) {
        DeliveryItems entity = mapper.toEntity(dto);
        DeliveryChallan challan = deliveryChallanRepository.findById(dto.getChallanId())
                .orElseThrow(() -> new ResourceNotFoundException("Challan not found with ID: " + dto.getChallanId()));
        entity.setChallan(challan);
        DeliveryItems saved = deliveryItemsRepository.save(entity);
        return mapper.toDto(saved);
    }

    public Page<DeliveryItemsDTOResponse> getAll(
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
        Page<DeliveryItems> page = deliveryItemsRepository.searchDeliveryItems(
                batchNo, name, productCode, orderNo, serialNo, unit, hsnCode, challanIds, pageable
        );

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Delivery Items found with given filters");
        }

        List<DeliveryItemsDTOResponse> dtoList = mapper.toDtoList(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public DeliveryItemsDTOResponse getById(Long id) {
        DeliveryItems entity = deliveryItemsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Item not found with id: " + id));
        return mapper.toDto(entity);
    }

    public DeliveryItemsDTOResponse update(Long id, DeliveryItemsDTO dto) {
        DeliveryItems existing = deliveryItemsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Item not found with id: " + id));

        DeliveryChallan challan = deliveryChallanRepository.findById(dto.getChallanId())
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
        existing.setQualitativeCheckPassedQty(dto.getQualitativeCheckPassedQty());
        existing.setQualitativeCheckFailedQty(dto.getQualitativeCheckFailedQty());
        existing.setQuantitativeCheckPassedQty(dto.getQuantitativeCheckPassedQty());
        existing.setQuantitativeCheckFailedQty(dto.getQuantitativeCheckFailedQty());

        DeliveryItems saved = deliveryItemsRepository.save(existing);
        return mapper.toDto(saved);
    }

    public DeliveryItemsDTOResponse patch(Long id, DeliveryItemsDTO dto) {
        DeliveryItems existing = deliveryItemsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Item not found with id: " + id));

        if (dto.getChallanId() != null) {
            DeliveryChallan challan = deliveryChallanRepository.findById(dto.getChallanId())
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
        if (dto.getQualitativeCheckPassedQty() != null) existing.setQualitativeCheckPassedQty(dto.getQualitativeCheckPassedQty());
        if (dto.getQualitativeCheckFailedQty() != null) existing.setQualitativeCheckFailedQty(dto.getQualitativeCheckFailedQty());
        if (dto.getQuantitativeCheckPassedQty() != null) existing.setQuantitativeCheckPassedQty(dto.getQuantitativeCheckPassedQty());
        if (dto.getQuantitativeCheckFailedQty() != null) existing.setQuantitativeCheckFailedQty(dto.getQuantitativeCheckFailedQty());

        DeliveryItems saved = deliveryItemsRepository.save(existing);
        return mapper.toDto(saved);
    }

    public void delete(Long id) {
        DeliveryItems existing = deliveryItemsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Item not found with id: " + id));
        deliveryItemsRepository.delete(existing);
    }
}
