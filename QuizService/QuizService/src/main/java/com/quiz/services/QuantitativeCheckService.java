package com.quiz.services;

import com.quiz.dto.QuantitativeCheckDTO;
import com.quiz.dto.QuantitativeCheckDTOResponse;
import com.quiz.entities.QuantitativeCheck;
import com.quiz.exception.ResourceNotFoundException;
import com.quiz.mapper.QuantitativeCheckMapper;
import com.quiz.repositories.QuantitativeCheckRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class QuantitativeCheckService {

    @Autowired
    private QuantitativeCheckRepository quantitativeCheckRepository;

    @Autowired
    private QuantitativeCheckMapper quantitativeCheckMapper;

    public void createAllQuantitativeCheck(List<QuantitativeCheckDTO> dtoList) {
        List<QuantitativeCheck> entities = quantitativeCheckMapper.quantitativeCheckDTOListToQuantitativeCheckList(dtoList);
        quantitativeCheckRepository.saveAll(entities);
    }

    public void createQuantitativeCheck(QuantitativeCheckDTO dto) {
        QuantitativeCheck entity = quantitativeCheckMapper.quantitativeCheckDTOToQuantitativeCheck(dto);
        quantitativeCheckRepository.save(entity);
    }

    public Page<QuantitativeCheckDTOResponse> getAllQuantitativeChecksWithFilters(
            String description,
            Boolean isScan,
            String status,
            String value,
            List<Long> quantitativeCheckMasterIds,
            Pageable pageable) {

        Page<QuantitativeCheck> page = quantitativeCheckRepository.searchQuantitativeChecksAdvanced(
                description, isScan, status, value, quantitativeCheckMasterIds, pageable);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Quantitative Checks found");
        }

        List<QuantitativeCheckDTOResponse> dtoList =
                quantitativeCheckMapper.quantitativeCheckListToQuantitativeCheckDTOResponseList(page.getContent());

        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public QuantitativeCheckDTOResponse getQuantitativeCheckById(Long id) {
        QuantitativeCheck entity = quantitativeCheckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quantitative Check not found with id: " + id));
        return quantitativeCheckMapper.quantitativeCheckToQuantitativeCheckDTOResponse(entity);
    }

    public QuantitativeCheckDTOResponse updateQuantitativeCheck(Long id, QuantitativeCheckDTO dto) {
        QuantitativeCheck entity = quantitativeCheckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quantitative Check not found with id: " + id));

        entity.setDescription(dto.getDescription());
        entity.setScan(dto.getScan());
        entity.setStatus(dto.getStatus());
        entity.setValue(dto.getValue());
        if (dto.getQuantitativeCheckMasterId() != null) {
            entity.getQuantitativeCheckMaster().setId(dto.getQuantitativeCheckMasterId());
        }

        QuantitativeCheck saved = quantitativeCheckRepository.save(entity);
        return quantitativeCheckMapper.quantitativeCheckToQuantitativeCheckDTOResponse(saved);
    }

    public QuantitativeCheckDTOResponse patchQuantitativeCheck(Long id, QuantitativeCheckDTO dto) {
        QuantitativeCheck entity = quantitativeCheckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quantitative Check not found with id: " + id));

        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getScan() != null) entity.setScan(dto.getScan());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getValue() != null) entity.setValue(dto.getValue());
        if (dto.getQuantitativeCheckMasterId() != null) {
            entity.getQuantitativeCheckMaster().setId(dto.getQuantitativeCheckMasterId());
        }

        QuantitativeCheck saved = quantitativeCheckRepository.save(entity);
        return quantitativeCheckMapper.quantitativeCheckToQuantitativeCheckDTOResponse(saved);
    }

    public void deleteQuantitativeCheck(Long id) {
        QuantitativeCheck entity = quantitativeCheckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quantitative Check not found with id: " + id));
        quantitativeCheckRepository.delete(entity);
    }
}
