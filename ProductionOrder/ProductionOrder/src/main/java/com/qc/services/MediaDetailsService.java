package com.qc.services;

import com.qc.config.IdentityClient;
import com.qc.dto.MediaDetailsDTO;
import com.qc.dto.MediaDetailsDTOResponse;
import com.qc.entities.Media;
import com.qc.entities.MediaDetails;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.MediaDetailsMapper;
import com.qc.repositories.MediaDetailsRepository;
import com.qc.repositories.MediaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class MediaDetailsService {
    @Autowired
    private MediaDetailsRepository mediaDetailsRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MediaDetailsMapper mediaDetailsMapper;

    @Autowired
    private IdentityClient identityClient;

    public void createAllMediaDetails(List<MediaDetailsDTO> dtoList, String token) {
        Map<String, Object> authData = identityClient.validateToken(token);
        String uploadedBy = (String) authData.get("username");

        List<MediaDetails> entities = mediaDetailsMapper.toEntityList(dtoList);
        entities.forEach(entity -> {
            Media media = mediaRepository.findById(entity.getMedia().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Media not found with ID: " + entity.getMedia().getId()));
            entity.setMedia(media);
            entity.setUploadedBy(uploadedBy);
        });
        mediaDetailsRepository.saveAll(entities);
        log.info("✅ Created {} media details by {}", entities.size(), uploadedBy);
    }

    public void createMediaDetails(MediaDetailsDTO dto, String token) {
        Map<String, Object> authData = identityClient.validateToken(token);
        String uploadedBy = (String) authData.get("username");

        MediaDetails entity = mediaDetailsMapper.toEntity(dto);
        Media media = mediaRepository.findById(dto.getMediaId())
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with ID: " + dto.getMediaId()));
        entity.setMedia(media);
        entity.setUploadedBy(uploadedBy);
        mediaDetailsRepository.save(entity);
        log.info("✅ Created media detail for mediaId={} by {}", dto.getMediaId(), uploadedBy);
    }

    public Page<MediaDetailsDTOResponse> getAllMediaDetails(Pageable pageable) {
        Page<MediaDetails> page = mediaDetailsRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No Media Details found");
        }
        List<MediaDetailsDTOResponse> dtoList = mediaDetailsMapper.toDtoList(page.getContent());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    public MediaDetailsDTOResponse getMediaDetailsById(Long id) {
        MediaDetails entity = mediaDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media Details not found with id: " + id));
        return mediaDetailsMapper.toDto(entity);
    }

    public MediaDetailsDTOResponse updateMediaDetails(Long id, MediaDetailsDTO dto, String token) {
        Map<String, Object> authData = identityClient.validateToken(token);
        String uploadedBy = (String) authData.get("username");

        MediaDetails existing = mediaDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media Details not found with id: " + id));

        Media media = mediaRepository.findById(dto.getMediaId())
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with ID: " + dto.getMediaId()));

        existing.setDescription(dto.getDescription());
        existing.setMediaFor(dto.getMediaFor());
        existing.setName(dto.getName());
        existing.setType(dto.getType());
        existing.setMedia(media);
        existing.setUploadedBy(uploadedBy);

        MediaDetails saved = mediaDetailsRepository.save(existing);
        return mediaDetailsMapper.toDto(saved);
    }

    public MediaDetailsDTOResponse patchMediaDetails(Long id, MediaDetailsDTO dto, String token) {
        Map<String, Object> authData = identityClient.validateToken(token);
        String uploadedBy = (String) authData.get("username");

        MediaDetails existing = mediaDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media Details not found with id: " + id));

        if (dto.getMediaId() != null) {
            Media media = mediaRepository.findById(dto.getMediaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Media not found with ID: " + dto.getMediaId()));
            existing.setMedia(media);
        }

        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getMediaFor() != null) existing.setMediaFor(dto.getMediaFor());
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getType() != null) existing.setType(dto.getType());
        existing.setUploadedBy(uploadedBy);

        MediaDetails saved = mediaDetailsRepository.save(existing);
        return mediaDetailsMapper.toDto(saved);
    }

    public void deleteMediaDetails(Long id) {
        MediaDetails existing = mediaDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media Details not found with id: " + id));
        mediaDetailsRepository.delete(existing);
    }
}
