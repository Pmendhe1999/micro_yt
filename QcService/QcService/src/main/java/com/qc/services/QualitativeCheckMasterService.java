package com.qc.services;

import com.qc.dto.QualitativeCheckMasterDTO;
import com.qc.dto.QualitativeCheckMasterDTOResponse;
import com.qc.entities.QualitativeCheckMaster;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.QualitativeCheckMasterMapper;
import com.qc.repositories.QualitativeCheckMasterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class QualitativeCheckMasterService {
    @Autowired
    private QualitativeCheckMasterRepository repository;

    @Autowired
    private QualitativeCheckMasterMapper mapper;

    public void createAllQualitativeCheckMaster(List<QualitativeCheckMasterDTO> dtoList) {
        List<QualitativeCheckMaster> entities = mapper.qualitativeCheckMasterDTOListToEntityList(dtoList);
        repository.saveAll(entities);
    }

    public void createQualitativeCheckMaster(QualitativeCheckMasterDTO dto) {
        QualitativeCheckMaster entity = mapper.qualitativeCheckMasterDTOToEntity(dto);
        repository.save(entity);
    }

    public Page<QualitativeCheckMasterDTOResponse> getAllQualitativeCheckMastersWithFilters(
            String name,
            String description,
            String status,
            QualitativeCheckMaster.CheckStatus checkStatus,
            List<Long> scanMasterIds,
            Pageable pageable) {

        Page<QualitativeCheckMaster> page = repository.searchQualitativeCheckMastersAdvanced(
                name, description, status, checkStatus, scanMasterIds, pageable);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Qualitative Check Masters found");
        }

        List<QualitativeCheckMasterDTOResponse> dtoList =
                mapper.entityListToQualitativeCheckMasterDTOResponseList(page.getContent());

        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public QualitativeCheckMasterDTOResponse getQualitativeCheckMasterById(Long id) {
        QualitativeCheckMaster entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Qualitative Check Master not found with id: " + id));
        return mapper.entityToQualitativeCheckMasterDTOResponse(entity);
    }

    public QualitativeCheckMasterDTOResponse updateQualitativeCheckMaster(Long id, QualitativeCheckMasterDTO dto) {
        QualitativeCheckMaster entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Qualitative Check Master not found with id: " + id));

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCheckStatus(dto.getCheckStatus());
        entity.setStatus(dto.getStatus());
        if (dto.getScanMasterId() != null) {
            entity.setScanMaster(mapper.mapScanMaster(dto.getScanMasterId()));
        }

        QualitativeCheckMaster updated = repository.save(entity);
        return mapper.entityToQualitativeCheckMasterDTOResponse(updated);
    }

    public void deleteQualitativeCheckMaster(Long id) {
        QualitativeCheckMaster entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Qualitative Check Master not found with id: " + id));
        repository.delete(entity);
    }
}
