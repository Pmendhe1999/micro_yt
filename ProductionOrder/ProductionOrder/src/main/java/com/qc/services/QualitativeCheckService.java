package com.qc.services;

import com.qc.dto.QualitativeCheckDTO;
import com.qc.dto.QualitativeCheckDTOResponse;
import com.qc.entities.Product;
import com.qc.entities.QualitativeCheck;
import com.qc.entities.QualitativeCheckMaster;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.QualitativeCheckMapper;
import com.qc.repositories.ProductRepository;
import com.qc.repositories.QualitativeCheckMasterRepository;
import com.qc.repositories.QualitativeCheckRepository;
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
    private ProductRepository productRepository; // ✅ added

    @Autowired
    private QualitativeCheckMapper qualitativeCheckMapper;

    public void createAllQualitativeCheck(List<QualitativeCheckDTO> dtoList) {
        List<QualitativeCheck> entities = qualitativeCheckMapper.qualitativeCheckDTOListToQualitativeCheckList(dtoList);
        qualitativeCheckRepository.saveAll(entities);
    }

    public QualitativeCheckDTOResponse createQualitativeCheck(QualitativeCheckDTO dto) {
        QualitativeCheck entity = qualitativeCheckMapper.qualitativeCheckDTOToQualitativeCheck(dto);

        // ✅ Validate foreign keys
        QualitativeCheckMaster master = qualitativeCheckMasterRepository.findById(dto.getQualitativeCheckMasterId())
                .orElseThrow(() -> new ResourceNotFoundException("QualitativeCheckMaster not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        entity.setQualitativeCheckMaster(master);
        entity.setProduct(product);

        qualitativeCheckRepository.save(entity);
        return qualitativeCheckMapper.qualitativeCheckToQualitativeCheckDTOResponse(entity);
    }

    public Page<QualitativeCheckDTOResponse> getAllQualitativeChecksWithFilters(
            String description,
            Boolean isScan,
            String status,
            String value,
            List<Long> qualitativeCheckMasterIds,
            List<Long> productIds, // ✅ added filter
            Pageable pageable) {

        Page<QualitativeCheck> page = qualitativeCheckRepository.searchQualitativeChecksAdvanced(
                description, isScan, status, value, qualitativeCheckMasterIds, productIds, pageable);

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

        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            entity.setProduct(product);
        }

        QualitativeCheck saved = qualitativeCheckRepository.save(entity);
        return qualitativeCheckMapper.qualitativeCheckToQualitativeCheckDTOResponse(saved);
    }

    public void deleteQualitativeCheck(Long id) {
        QualitativeCheck entity = qualitativeCheckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Qualitative Check not found with id: " + id));
        qualitativeCheckRepository.delete(entity);
    }
}
