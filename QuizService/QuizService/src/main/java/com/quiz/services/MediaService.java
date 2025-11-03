package com.quiz.services;

import com.quiz.config.IdentityClient;
import com.quiz.dto.MediaDTO;
import com.quiz.dto.MediaDTOResponse;
import com.quiz.entities.Media;
import com.quiz.exception.ResourceNotFoundException;
import com.quiz.mapper.MediaMapper;
import com.quiz.repositories.MediaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private IdentityClient identityClient;

    public void createAllMedia(List<MediaDTO> dtoList, String token) {
        Map<String, Object> authData = identityClient.validateToken(token);
        String uploadedBy = (String) authData.get("username");

        List<Media> entities = mediaMapper.toEntityList(dtoList);
        entities.forEach(entity -> {
            entity.setUploadedBy(uploadedBy);

        });

        mediaRepository.saveAll(entities);
    }

    public MediaDTOResponse createMedia(MediaDTO dto, String token) {
        Map<String, Object> authData = identityClient.validateToken(token);
        String uploadedBy = (String) authData.get("username");

        Media entity = mediaMapper.toEntity(dto);
        entity.setUploadedBy(uploadedBy);


        Media saved = mediaRepository.save(entity);
        return mediaMapper.toDto(saved);
    }

    public Page<MediaDTOResponse> getAllMedia(Pageable pageable) {
        Page<Media> page = mediaRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No media records found");
        }
        List<MediaDTOResponse> dtoList = mediaMapper.toDtoList(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public MediaDTOResponse getMediaById(Long id) {
        Media entity = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + id));
        return mediaMapper.toDto(entity);
    }

    public MediaDTOResponse updateMedia(Long id, MediaDTO dto, String token) {
        Media existing = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + id));

        Map<String, Object> authData = identityClient.validateToken(token);
        String uploadedBy = (String) authData.get("username");

        existing.setBaseImageUrl(dto.getBaseImageUrl());
        existing.setDescription(dto.getDescription());
        existing.setName(dto.getName());
        existing.setStatus(dto.getStatus());
        existing.setType(dto.getType());
        existing.setUploadedBy(uploadedBy);


        Media saved = mediaRepository.save(existing);
        return mediaMapper.toDto(saved);
    }

    public MediaDTOResponse patchMedia(Long id, MediaDTO dto, String token) {
        Media existing = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + id));

        Map<String, Object> authData = identityClient.validateToken(token);
        String uploadedBy = (String) authData.get("username");

        if (dto.getBaseImageUrl() != null) existing.setBaseImageUrl(dto.getBaseImageUrl());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
        if (dto.getType() != null) existing.setType(dto.getType());
        existing.setUploadedBy(uploadedBy);


        Media saved = mediaRepository.save(existing);
        return mediaMapper.toDto(saved);
    }

    public void deleteMedia(Long id) {
        Media existing = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + id));
        mediaRepository.delete(existing);
    }
}
