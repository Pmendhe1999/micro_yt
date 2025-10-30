package com.quiz.services;

import com.quiz.dto.QualitativeCheckMasterDTO;
import com.quiz.dto.QualitativeCheckMasterDTOResponse;
import com.quiz.entities.QualitativeCheckMaster;
import com.quiz.exception.ResourceNotFoundException;
import com.quiz.mapper.QualitativeCheckMasterMapper;
import com.quiz.repositories.QualitativeCheckMasterRepository;
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

    public Page<QualitativeCheckMasterDTOResponse> getAllQualitativeCheckMasters(Pageable pageable) {
        Page<QualitativeCheckMaster> page = repository.findAll(pageable);
        List<QualitativeCheckMasterDTOResponse> dtoList = mapper.entityListToQualitativeCheckMasterDTOResponseList(page.getContent());
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
