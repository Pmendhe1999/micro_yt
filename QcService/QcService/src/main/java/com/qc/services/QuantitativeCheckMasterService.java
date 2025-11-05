package com.qc.services;

import com.qc.dto.QuantitativeCheckMasterDTO;
import com.qc.dto.QuantitativeCheckMasterDTOResponse;
import com.qc.entities.QuantitativeCheckMaster;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.QuantitativeCheckMasterMapper;
import com.qc.repositories.QuantitativeCheckMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class QuantitativeCheckMasterService {
    @Autowired
    private QuantitativeCheckMasterRepository quantitativeCheckMasterRepository;

    @Autowired
    private QuantitativeCheckMasterMapper quantitativeCheckMasterMapper;

    public void createAll(List<QuantitativeCheckMasterDTO> dtoList) {
        List<QuantitativeCheckMaster> entities = quantitativeCheckMasterMapper.toEntityList(dtoList);
        quantitativeCheckMasterRepository.saveAll(entities);
    }

    public QuantitativeCheckMasterDTOResponse create(QuantitativeCheckMasterDTO dto) {
        QuantitativeCheckMaster entity = quantitativeCheckMasterMapper.toEntity(dto);
        QuantitativeCheckMaster saved = quantitativeCheckMasterRepository.save(entity);
        return quantitativeCheckMasterMapper.toResponse(saved);
    }

    public Page<QuantitativeCheckMasterDTOResponse> getAllWithFilters(
            String name,
            String description,
            QuantitativeCheckMaster.CheckStatus checkStatus,
            String status,
            List<Long> scanMasterIds,
            Pageable pageable) {

        Page<QuantitativeCheckMaster> page = quantitativeCheckMasterRepository.searchQuantitativeCheckMasterAdvanced(
                name, description, checkStatus, status, scanMasterIds, pageable);

        List<QuantitativeCheckMasterDTOResponse> dtoList =
                quantitativeCheckMasterMapper.toResponseList(page.getContent());

        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }
    public QuantitativeCheckMasterDTOResponse getById(Long id) {
        QuantitativeCheckMaster entity = quantitativeCheckMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quantitative Check Master not found with id: " + id));
        return quantitativeCheckMasterMapper.toResponse(entity);
    }

    public QuantitativeCheckMasterDTOResponse update(Long id, QuantitativeCheckMasterDTO dto) {
        QuantitativeCheckMaster entity = quantitativeCheckMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quantitative Check Master not found with id: " + id));

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCheckStatus(dto.getCheckStatus());
        entity.setStatus(dto.getStatus());
        if (dto.getScanMasterId() != null) {
            entity.getScanMaster().setId(dto.getScanMasterId());
        }

        QuantitativeCheckMaster updated = quantitativeCheckMasterRepository.save(entity);
        return quantitativeCheckMasterMapper.toResponse(updated);
    }

    public void delete(Long id) {
        QuantitativeCheckMaster entity = quantitativeCheckMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quantitative Check Master not found with id: " + id));
        quantitativeCheckMasterRepository.delete(entity);
    }
}
