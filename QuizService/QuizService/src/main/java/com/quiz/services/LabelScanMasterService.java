package com.quiz.services;

import com.quiz.dto.LabelScanMasterDTO;
import com.quiz.dto.LabelScanMasterDTOResponse;
import com.quiz.entities.LabelScanMaster;
import com.quiz.exception.ResourceNotFoundException;
import com.quiz.mapper.LabelScanMasterMapper;
import com.quiz.repositories.LabelScanMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LabelScanMasterService {
    @Autowired
    private LabelScanMasterRepository labelScanMasterRepository;

    @Autowired
    private LabelScanMasterMapper labelScanMasterMapper;

    public void createAllLabelScanMaster(List<LabelScanMasterDTO> labelScanMasterDTOList) {
        List<LabelScanMaster> entities = labelScanMasterMapper
                .labelScanMasterDTOListToLabelScanMasterList(labelScanMasterDTOList);
        labelScanMasterRepository.saveAll(entities);
    }

    public void createLabelScanMaster(LabelScanMasterDTO dto) {
        LabelScanMaster entity = labelScanMasterMapper.labelScanMasterDTOToLabelScanMaster(dto);
        labelScanMasterRepository.save(entity);
    }

    public Page<LabelScanMasterDTOResponse> getAllLabelScanMasterWithFilters(
            String name,
            String description,
            String scanType,
            LabelScanMaster.CheckStatus checkStatus,
            String status,
            Pageable pageable) {

        Page<LabelScanMaster> page = labelScanMasterRepository.searchLabelScanMasters(
                name, description, scanType, checkStatus, status, pageable);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Label Scan Masters found");
        }

        List<LabelScanMasterDTOResponse> dtoList =
                labelScanMasterMapper.labelScanMasterListToLabelScanMasterDTOResponseList(page.getContent());

        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public LabelScanMasterDTOResponse getLabelScanMasterById(Long id) {
        LabelScanMaster entity = labelScanMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label Scan Master not found with id: " + id));
        return labelScanMasterMapper.labelScanMasterToLabelScanMasterDTOResponse(entity);
    }

    public LabelScanMasterDTOResponse updateLabelScanMaster(Long id, LabelScanMasterDTO dto) {
        LabelScanMaster existing = labelScanMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label Scan Master not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setScanType(dto.getScanType());
        existing.setSeqNumber(dto.getSeqNumber());
        existing.setCheckStatus(dto.getCheckStatus());
        existing.setStatus(dto.getStatus());

        LabelScanMaster saved = labelScanMasterRepository.save(existing);
        return labelScanMasterMapper.labelScanMasterToLabelScanMasterDTOResponse(saved);
    }

    public LabelScanMasterDTOResponse patchLabelScanMaster(Long id, LabelScanMasterDTO dto) {
        LabelScanMaster existing = labelScanMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label Scan Master not found with id: " + id));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getScanType() != null) existing.setScanType(dto.getScanType());
        if (dto.getSeqNumber() != null) existing.setSeqNumber(dto.getSeqNumber());
        if (dto.getCheckStatus() != null) existing.setCheckStatus(dto.getCheckStatus());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());

        LabelScanMaster saved = labelScanMasterRepository.save(existing);
        return labelScanMasterMapper.labelScanMasterToLabelScanMasterDTOResponse(saved);
    }

    public void deleteLabelScanMaster(Long id) {
        LabelScanMaster existing = labelScanMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label Scan Master not found with id: " + id));
        labelScanMasterRepository.delete(existing);
    }
}
