package com.identity.serviceImpl;

import com.identity.dto.MediaDetailsDTO;
import com.identity.entity.Media;
import com.identity.entity.MediaDetails;
import com.identity.entity.UserCredential;
import com.identity.reository.MediaDetailsRepository;
import com.identity.reository.MediaRepository;
import com.identity.reository.UserRepository;
import com.identity.service.JwtService;
import com.identity.service.MediaDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MediaDetailsServiceImpl implements MediaDetailsService {

    @Autowired
    private MediaDetailsRepository repository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(MediaDetailsServiceImpl.class);


    @Override
    public MediaDetails saveMediaDetails(MediaDetailsDTO dto, String token) {
        try {
            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            Media media = mediaRepository.findById(dto.getMediaId())
                    .orElseThrow(() -> new NoSuchElementException("Media not found"));
            UserCredential user = userRepository.findByUserId(dto.getUserId())
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

            MediaDetails details = new MediaDetails();
            details.setName(dto.getName());
            details.setType(dto.getType());
            details.setDescription(dto.getDescription());
            details.setMediaFor(dto.getMediaFor());
            details.setMedia(media);
            details.setUser(user);
            details.setCreatedDate(LocalDateTime.now());
            details.setLastModifiedDate(LocalDateTime.now());

            MediaDetails saved = repository.save(details);
            log.info("MediaDetails '{}' created for mediaId={} by user={} role={}",
                    saved.getName(), media.getId(), createdByUser, role);

            return saved;
        } catch (Exception e) {
            log.error("Error while saving MediaDetails: {}", e.getMessage(), e);
            throw new RuntimeException("Error while saving MediaDetails: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<MediaDetails> getAllMediaDetails(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                return repository.findByNameContainingIgnoreCase(search, pageable);
            }
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error while fetching MediaDetails: {}", e.getMessage(), e);
            throw new RuntimeException("Error while fetching MediaDetails: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<MediaDetails> getMediaDetailsById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error while fetching MediaDetails id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error while fetching MediaDetails: " + e.getMessage(), e);
        }
    }

    @Override
    public MediaDetails updateMediaDetailsReturnEntity(Long id, MediaDetailsDTO dto, String token) {
        try {
            MediaDetails existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("MediaDetails not found with id " + id));

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            Media media = mediaRepository.findById(dto.getMediaId())
                    .orElseThrow(() -> new NoSuchElementException("Media not found"));
            UserCredential user = userRepository.findByUserId(dto.getUserId())
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

            existing.setName(dto.getName());
            existing.setType(dto.getType());
            existing.setDescription(dto.getDescription());
            existing.setMediaFor(dto.getMediaFor());
            existing.setMedia(media);
            existing.setUser(user);
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);
            log.info("MediaDetails id={} updated by user={} role={}", id, modifiedByUser, role);

            return existing;
        } catch (Exception e) {
            log.error("Error while updating MediaDetails id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error while updating MediaDetails: " + e.getMessage(), e);
        }
    }

    @Override
    public MediaDetails deleteMediaDetailsReturnEntity(Long id, String token) {
        try {
            MediaDetails existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("MediaDetails not found with id " + id));

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);
            log.info("MediaDetails id={} deleted by user={} role={}", id, deletedByUser, role);

            return existing;
        } catch (Exception e) {
            log.error("Error while deleting MediaDetails id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error while deleting MediaDetails: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MediaDetails> getMediaDetailsByUserId(Long userId) {
        try {
            return repository.findByUser_UserId(userId);
        } catch (Exception e) {
            log.error("Error while fetching MediaDetails by userId={}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Error while fetching MediaDetails by userId: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MediaDetails> getMediaDetailsByMediaId(Long mediaId) {
        try {
            return repository.findByMedia_Id(mediaId);
        } catch (Exception e) {
            log.error("Error while fetching MediaDetails by mediaId={}: {}", mediaId, e.getMessage(), e);
            throw new RuntimeException("Error while fetching MediaDetails by mediaId: " + e.getMessage(), e);
        }
    }
}
