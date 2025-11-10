package com.qc.services;

import com.qc.dto.DeliveryChallanMasterDTO;
import com.qc.dto.DeliveryChallanMasterDTOResponse;
import com.qc.entities.DeliveryChallanMaster;
import com.qc.entities.DeliveryItemsMaster;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.DeliveryChallanMasterMapper;
import com.qc.repositories.DeliveryChallanMasterRepository;
import com.qc.repositories.DeliveryItemsMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeliveryChallanMasterService {
    @Autowired
    private DeliveryChallanMasterRepository repository;

    @Autowired
    private DeliveryChallanMasterMapper mapper;

    @Autowired
    private DeliveryItemsMasterRepository itemsRepo;

    public void createAllDeliveryChallanMaster(List<DeliveryChallanMasterDTO> dtoList) {
        List<DeliveryChallanMaster> entities = mapper.toEntityList(dtoList);
        repository.saveAll(entities);
    }

    public void createDeliveryChallanMaster(DeliveryChallanMasterDTO dto) {
        DeliveryChallanMaster entity = mapper.toEntity(dto);
        repository.save(entity);
    }

    public void createDeliveryChallanWithItems(DeliveryChallanMasterDTO dto) {
        // 1️⃣ Create DeliveryChallanMaster
        DeliveryChallanMaster challan = new DeliveryChallanMaster();
        challan.setName(dto.getName());
        challan.setDescriptions(dto.getDescriptions());
        challan.setStatus(dto.getStatus() != null ? dto.getStatus() : true);

        challan = repository.save(challan);

        // ✅ Create final reference for lambda
        final DeliveryChallanMaster savedChallan = challan;

        // 2️⃣ Create DeliveryItems for this challan
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            List<DeliveryItemsMaster> items = dto.getItems().stream()
                    .map(itemDto -> {
                        DeliveryItemsMaster item = new DeliveryItemsMaster();
                        item.setBatchNo(itemDto.getBatchNo());
                        item.setDescription(itemDto.getDescription());
                        item.setExpDate(itemDto.getExpDate());
                        item.setHsnCode(itemDto.getHsnCode());
                        item.setMfgDate(itemDto.getMfgDate());
                        item.setName(itemDto.getName());
                        item.setOrderNo(itemDto.getOrderNo());
                        item.setProductCode(itemDto.getProductCode());
                        item.setQuantity(itemDto.getQuantity());
                        item.setSerialNo(itemDto.getSerialNo());
                        item.setUnit(itemDto.getUnit());
                        item.setChallan(savedChallan);
                        return item;
                    })
                    .collect(Collectors.toList());

            itemsRepo.saveAll(items);
        }

    }

    public Page<DeliveryChallanMasterDTOResponse> getAllDeliveryChallanMaster(
            String name,
            String descriptions,
            Boolean status,
            Pageable pageable) {

        Page<DeliveryChallanMaster> page =
                repository.searchDeliveryChallans(name, descriptions, status, pageable);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Delivery Challans found");
        }

        List<DeliveryChallanMasterDTOResponse> dtoList = mapper.toDtoList(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public DeliveryChallanMasterDTOResponse getDeliveryChallanMasterById(Long id) {
        DeliveryChallanMaster entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));
        return mapper.toDto(entity);
    }

    public DeliveryChallanMasterDTOResponse updateDeliveryChallanMaster(Long id, DeliveryChallanMasterDTO dto) {
        DeliveryChallanMaster existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescriptions(dto.getDescriptions());
        existing.setStatus(dto.getStatus());

        DeliveryChallanMaster saved = repository.save(existing);
        return mapper.toDto(saved);
    }

    public DeliveryChallanMasterDTOResponse patchDeliveryChallanMaster(Long id, DeliveryChallanMasterDTO dto) {
        DeliveryChallanMaster existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getDescriptions() != null) existing.setDescriptions(dto.getDescriptions());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());

        DeliveryChallanMaster saved = repository.save(existing);
        return mapper.toDto(saved);
    }

    public void deleteDeliveryChallanMaster(Long id) {
        DeliveryChallanMaster existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));
        repository.delete(existing);
    }
}
