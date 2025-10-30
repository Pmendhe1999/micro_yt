package com.quiz.services;

import com.quiz.dto.QualitativeCheckDTO;
import com.quiz.dto.QualitativeCheckDTOResponse;
import com.quiz.entities.QualitativeCheck;
import com.quiz.exception.ResourceNotFoundException;
import com.quiz.mapper.QualitativeCheckMapper;
import com.quiz.repositories.QualitativeCheckMasterRepository;
import com.quiz.repositories.QualitativeCheckRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class QualitativeCheckService {

    @Autowired
    private QualitativeCheckRepository qualitativeCheckRepository;

    @Autowired
    private QualitativeCheckMasterRepository qualitativeCheckMasterRepository;

    @Autowired
    private QualitativeCheckMapper qualitativeCheckMapper;

    public void createAllQualitativeCheck(List<QualitativeCheckDTO> dtoList) {
        List<QualitativeCheck> entities = qualitativeCheckMapper.qualitativeCheckDTOListToQualitativeCheckList(dtoList);
        qualitativeCheckRepository.saveAll(entities);
    }

    public QualitativeCheckDTOResponse createQualitativeCheck(QualitativeCheckDTO dto) {
        QualitativeCheck entity = qualitativeCheckMapper.qualitativeCheckDTOToQualitativeCheck(dto);
        qualitativeCheckRepository.save(entity);
        return qualitativeCheckMapper.qualitativeCheckToQualitativeCheckDTOResponse(entity);
    }

    public Page<QualitativeCheckDTOResponse> getAllQualitativeChecks(Pageable pageable) {
        Page<QualitativeCheck> page = qualitativeCheckRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Qualitative Checks found");
        }
        List<QualitativeCheckDTOResponse> dtoList =
                qualitativeCheckMapper.qualitativeCheckListToQualitativeCheckDTOResponseList(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public QualitativeCheckDTOResponse getQualitativeCheckById(Long id) {
        QualitativeCheck entity = qualitativeCheckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Qualitative Check not found with id: " + id));
        return qualitativeCheckMapper.qualitativeCheckToQualitativeCheckDTOResponse(entity);
    }

    public QualitativeCheckDTOResponse updateQualitativeCheck(Long id, QualitativeCheckDTO dto) {
        QualitativeCheck entity = qualitativeCheckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Qualitative Check not found with id: " + id));

        entity.setDescription(dto.getDescription());
        entity.setScan(dto.getScan());
        entity.setStatus(dto.getStatus());
        entity.setValue(dto.getValue());

        QualitativeCheck saved = qualitativeCheckRepository.save(entity);
        return qualitativeCheckMapper.qualitativeCheckToQualitativeCheckDTOResponse(saved);
    }

    public QualitativeCheckDTOResponse patchQualitativeCheck(Long id, QualitativeCheckDTO dto) {
        QualitativeCheck entity = qualitativeCheckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Qualitative Check not found with id: " + id));

        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getScan() != null) entity.setScan(dto.getScan());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getValue() != null) entity.setValue(dto.getValue());

        QualitativeCheck saved = qualitativeCheckRepository.save(entity);
        return qualitativeCheckMapper.qualitativeCheckToQualitativeCheckDTOResponse(saved);
    }

    public void deleteQualitativeCheck(Long id) {
        QualitativeCheck entity = qualitativeCheckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Qualitative Check not found with id: " + id));
        qualitativeCheckRepository.delete(entity);
    }
}
