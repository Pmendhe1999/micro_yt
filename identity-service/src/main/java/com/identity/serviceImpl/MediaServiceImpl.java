package com.identity.serviceImpl;

import com.identity.dto.MediaDTO;
import com.identity.entity.Media;
import com.identity.reository.MediaRepository;
import com.identity.service.JwtService;
import com.identity.service.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaRepository repository;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(MediaServiceImpl.class);

    @Override
    public Media saveMedia(MediaDTO mediaDTO, String token) {
        try {
            if (repository.existsByName(mediaDTO.getName())) {
                log.warn("Attempt to create duplicate Media: {}", mediaDTO.getName());
                throw new IllegalArgumentException("Media already exists");
            }

            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            Media media = new Media();
            media.setName(mediaDTO.getName());
            media.setDescription(mediaDTO.getDescription());
            media.setType(mediaDTO.getType());
            media.setStatus(mediaDTO.getStatus());
            media.setBaseImageUrl(mediaDTO.getBaseImageUrl());
            media.setUploadedBy(createdByUser);
            media.setCreatedDate(LocalDateTime.now());
            media.setLastModifiedDate(LocalDateTime.now());

            Media savedMedia = repository.save(media);

            log.info("Media '{}' created by user={} role={}", savedMedia.getName(), createdByUser, role);

            return savedMedia;

        } catch (IllegalArgumentException e) {
            log.error("Business validation failed while saving Media: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while saving Media: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving Media: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Media> getAllMedia(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                log.debug("Fetching Media with search filter: {}", search);
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            log.debug("Fetching all Media without filter");
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching Media: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Media: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Media> getMediaById(Long id) {
        try {
            log.debug("Fetching Media by id={}", id);
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching Media with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Media with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Media updateMediaReturnEntity(Long id, MediaDTO updatedMediaDTO, String token) {
        try {
            Media existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Media not found with id " + id));

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            existing.setName(updatedMediaDTO.getName());
            existing.setDescription(updatedMediaDTO.getDescription());
            existing.setType(updatedMediaDTO.getType());
            existing.setStatus(updatedMediaDTO.getStatus());
            existing.setBaseImageUrl(updatedMediaDTO.getBaseImageUrl());
            existing.setUploadedBy(modifiedByUser);
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);

            log.info("Media id={} updated by user={} role={}", id, modifiedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating Media id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating Media with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Media deleteMediaReturnEntity(Long id, String token) {
        try {
            Media existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Media not found with id " + id));

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("Media id={} deleted by user={} role={}", id, deletedByUser, role);

            return existing;

        } catch (NoSuchElementException e) {
            log.error("Delete failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while deleting Media id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting Media with id " + id + ": " + e.getMessage(), e);
        }
    }
}
