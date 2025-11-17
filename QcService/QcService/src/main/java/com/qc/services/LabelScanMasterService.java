package com.qc.services;

import com.qc.dto.*;
import com.qc.entities.*;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.LabelScanMasterMapper;
import com.qc.mapper.QualitativeCheckMasterMapper;
import com.qc.mapper.QuantitativeCheckMasterMapper;
import com.qc.repositories.LabelScanMasterRepository;
import com.qc.repositories.QualitativeCheckMasterRepository;
import com.qc.repositories.QuantitativeCheckMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LabelScanMasterService {
    @Autowired
    private LabelScanMasterRepository labelScanMasterRepository;

    @Autowired
    private LabelScanMasterMapper labelScanMasterMapper;

    @Autowired
    private QualitativeCheckMasterRepository qualitativeRepo;

    @Autowired
    private QuantitativeCheckMasterRepository quantitativeRepo;

    @Autowired
    private QualitativeCheckMasterMapper qualitativeMapper;

    @Autowired
    private QuantitativeCheckMasterMapper quantitativeMapper;

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
            Boolean status,
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

    public LabelScanMasterFullResponse getFullScanMasterDetails(Long id) {
        LabelScanMaster labelScanMaster = labelScanMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label Scan Master not found with id: " + id));

        // Fetch qualitative and quantitative checks
        List<QualitativeCheckMaster> qualitativeEntities = qualitativeRepo.findByScanMasterId(id);
        List<QuantitativeCheckMaster> quantitativeEntities = quantitativeRepo.findByScanMasterId(id);

        List<QualitativeCheckMasterDTOResponse> qualitativeDTOs =
                qualitativeMapper.entityListToQualitativeCheckMasterDTOResponseList(qualitativeEntities);
        List<QuantitativeCheckMasterDTOResponse> quantitativeDTOs =
                quantitativeMapper.toResponseList(quantitativeEntities);
        // Build response
        return new LabelScanMasterFullResponse(
                labelScanMaster.getId(),
                labelScanMaster.getName(),
                labelScanMaster.getDescription(),
                labelScanMaster.getScanType(),
                labelScanMaster.getSeqNumber(),
                labelScanMaster.getCheckStatus(),
                labelScanMaster.getStatus(),
                qualitativeDTOs,
                quantitativeDTOs
        );
    }

    public List<LabelScanMasterFullResponse> getAllFullScanMasterDetails() {

        // Step 1️⃣: Fetch only active LabelScanMasters (status = true)
        List<LabelScanMaster> labelScanMasters = labelScanMasterRepository.findByStatusTrue();

        // Step 2️⃣: Map each LabelScanMaster with its related checks
        return labelScanMasters.stream().map(scanMaster -> {

            List<QualitativeCheckMaster> qualitativeEntities =
                    qualitativeRepo.findByScanMasterId(scanMaster.getId());

            List<QuantitativeCheckMaster> quantitativeEntities =
                    quantitativeRepo.findByScanMasterId(scanMaster.getId());

            List<QualitativeCheckMasterDTOResponse> qualitativeDTOs =
                    qualitativeMapper.entityListToQualitativeCheckMasterDTOResponseList(qualitativeEntities);

            List<QuantitativeCheckMasterDTOResponse> quantitativeDTOs =
                    quantitativeMapper.toResponseList(quantitativeEntities);

            // Build and return the full response for this scan master
            return new LabelScanMasterFullResponse(
                    scanMaster.getId(),
                    scanMaster.getName(),
                    scanMaster.getDescription(),
                    scanMaster.getScanType(),
                    scanMaster.getSeqNumber(),
                    scanMaster.getCheckStatus(),
                    scanMaster.getStatus(),
                    qualitativeDTOs,
                    quantitativeDTOs
            );

        }).collect(Collectors.toList());
    }


}
