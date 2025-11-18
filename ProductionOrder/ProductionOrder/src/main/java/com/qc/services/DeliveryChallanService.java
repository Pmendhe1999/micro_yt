package com.qc.services;

import com.qc.dto.DeliveryChallanDTO;
import com.qc.dto.DeliveryChallanDTOResponse;
import com.qc.entities.DeliveryChallan;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.DeliveryChallanMapper;
import com.qc.repositories.DeliveryChallanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class DeliveryChallanService {
    @Autowired
    private DeliveryChallanRepository deliveryChallanRepository;

    @Autowired
    private DeliveryChallanMapper deliveryChallanMapper;

    // Create multiple
    public void createAllDeliveryChallan(List<DeliveryChallanDTO> dtoList) {
        List<DeliveryChallan> entities = deliveryChallanMapper.toEntityList(dtoList);
        deliveryChallanRepository.saveAll(entities);
    }

    // Create single
    public void createDeliveryChallan(DeliveryChallanDTO dto) {
        DeliveryChallan entity = deliveryChallanMapper.toEntity(dto);
        deliveryChallanRepository.save(entity);
    }

    // Get all
    public Page<DeliveryChallanDTOResponse> getAllDeliveryChallanWithFilters(
            String name,
            String descriptions,
            Boolean status,
            Pageable pageable
    ) {
        Page<DeliveryChallan> page = deliveryChallanRepository.searchDeliveryChallansAdvanced(
                name, descriptions, status, pageable
        );

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Delivery Challans found with given filters");
        }

        List<DeliveryChallanDTOResponse> dtoList = deliveryChallanMapper.toDtoList(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    // Get by ID
    public DeliveryChallanDTOResponse getDeliveryChallanById(Long id) {
        DeliveryChallan entity = deliveryChallanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));
        return deliveryChallanMapper.toDto(entity);
    }

    // Update
    public DeliveryChallanDTOResponse updateDeliveryChallan(Long id, DeliveryChallanDTO dto) {
        DeliveryChallan existing = deliveryChallanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescriptions(dto.getDescriptions());
        existing.setStatus(dto.getStatus());

        DeliveryChallan saved = deliveryChallanRepository.save(existing);
        return deliveryChallanMapper.toDto(saved);
    }

    // Patch
    public DeliveryChallanDTOResponse patchDeliveryChallan(Long id, DeliveryChallanDTO dto) {
        DeliveryChallan existing = deliveryChallanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getDescriptions() != null) existing.setDescriptions(dto.getDescriptions());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());

        DeliveryChallan saved = deliveryChallanRepository.save(existing);
        return deliveryChallanMapper.toDto(saved);
    }

    // Delete
    public void deleteDeliveryChallan(Long id) {
        DeliveryChallan existing = deliveryChallanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Challan not found with id: " + id));
        deliveryChallanRepository.delete(existing);
    }
}
