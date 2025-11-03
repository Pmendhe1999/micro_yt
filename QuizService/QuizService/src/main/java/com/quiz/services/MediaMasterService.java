package com.quiz.services;

import com.quiz.config.IdentityClient;
import com.quiz.dto.MediaMasterDTO;
import com.quiz.dto.MediaMasterDTOResponse;
import com.quiz.entities.MediaMaster;
import com.quiz.entities.ProductMaster;
import com.quiz.exception.ResourceNotFoundException;
import com.quiz.mapper.MediaMasterMapper;
import com.quiz.repositories.MediaMasterRepository;
import com.quiz.repositories.ProductMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class MediaMasterService {
    @Autowired
    private MediaMasterRepository mediaMasterRepository;

    @Autowired
    private ProductMasterRepository productMasterRepository;

    @Autowired
    private MediaMasterMapper mediaMasterMapper;

    @Autowired
    private IdentityClient identityClient;

    // ✅ Create single record
    public void createMedia(MediaMasterDTO dto, String token) {
        Map<String, Object> authData = identityClient.validateToken(token);
        String uploadedBy = (String) authData.get("username");

        ProductMaster product = productMasterRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));

        MediaMaster entity = mediaMasterMapper.toEntity(dto);
        entity.setProduct(product);
        entity.setUploadedBy(uploadedBy);

        mediaMasterRepository.save(entity);
        log.info("✅ Media uploaded by {} for product {}", uploadedBy, product.getName());
    }

    // ✅ Create all
    public void createAllMedia(List<MediaMasterDTO> dtoList, String token) {
        Map<String, Object> authData = identityClient.validateToken(token);
        String uploadedBy = (String) authData.get("username");

        List<MediaMaster> entities = mediaMasterMapper.toEntityList(dtoList);
        entities.forEach(entity -> {
            ProductMaster product = productMasterRepository.findById(entity.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + entity.getProduct().getId()));
            entity.setProduct(product);
            entity.setUploadedBy(uploadedBy);
        });

        mediaMasterRepository.saveAll(entities);
        log.info("✅ {} media entries created by {}", entities.size(), uploadedBy);
    }

    // ✅ Get all
    public Page<MediaMasterDTOResponse> getAllMedia(Pageable pageable) {
        Page<MediaMaster> page = mediaMasterRepository.findAll(pageable);
        if (page.isEmpty()) throw new ResourceNotFoundException("No Media entries found");
        return new PageImpl<>(mediaMasterMapper.toDtoList(page.getContent()), pageable, page.getTotalElements());
    }

    // ✅ Get by ID
    public MediaMasterDTOResponse getMediaById(Long id) {
        MediaMaster entity = mediaMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with ID: " + id));
        return mediaMasterMapper.toDto(entity);
    }

    // ✅ Update
    public MediaMasterDTOResponse updateMedia(Long id, MediaMasterDTO dto, String token) {
        MediaMaster existing = mediaMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with ID: " + id));

        ProductMaster product = productMasterRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));

        existing.setProduct(product);
        existing.setBaseImageUrl(dto.getBaseImageUrl());
        existing.setDescription(dto.getDescription());
        existing.setName(dto.getName());
        existing.setStatus(dto.getStatus());
        existing.setType(dto.getType());

        Map<String, Object> authData = identityClient.validateToken(token);
        existing.setUploadedBy((String) authData.get("username"));

        MediaMaster saved = mediaMasterRepository.save(existing);
        return mediaMasterMapper.toDto(saved);
    }

    // ✅ Patch
    public MediaMasterDTOResponse patchMedia(Long id, MediaMasterDTO dto, String token) {
        MediaMaster existing = mediaMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with ID: " + id));

        if (dto.getProductId() != null) {
            ProductMaster product = productMasterRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));
            existing.setProduct(product);
        }
        if (dto.getBaseImageUrl() != null) existing.setBaseImageUrl(dto.getBaseImageUrl());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        if (dto.getType() != null) existing.setType(dto.getType());

        Map<String, Object> authData = identityClient.validateToken(token);
        existing.setUploadedBy((String) authData.get("username"));

        MediaMaster saved = mediaMasterRepository.save(existing);
        return mediaMasterMapper.toDto(saved);
    }

    // ✅ Delete
    public void deleteMedia(Long id) {
        MediaMaster existing = mediaMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with ID: " + id));
        mediaMasterRepository.delete(existing);
    }

}
