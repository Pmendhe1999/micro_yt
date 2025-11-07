package com.identity.serviceImpl;

import com.identity.dto.MediaDTO;
import com.identity.entity.Media;
import com.identity.entity.MediaDetails;
import com.identity.entity.UserCredential;
import com.identity.reository.ApplicationRepository;
import com.identity.reository.MediaDetailsRepository;
import com.identity.reository.MediaRepository;
import com.identity.reository.UserCredentialRepository;
import com.identity.service.JwtService;
import com.identity.service.MediaService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private  AmazonS3 s3Client;
    @Autowired
    private  MediaRepository mediaRepository;

    @Autowired
    private  MediaDetailsRepository mediaDetailsRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private MediaRepository repository;

    @Autowired
    private UserCredentialRepository userRepository;

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


    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Transactional
    @Override
    public MediaDetails uploadMedia(Long userId, Long applicationId, MultipartFile file, String description, String mediaFor) {
        try {
            // 1️⃣ Identify target (user or application)
            MediaDetails existingMediaDetails = null;
            if (userId != null) {
                existingMediaDetails = mediaDetailsRepository.findByUserUserId(userId).orElse(null);
            } else if (applicationId != null) {
                existingMediaDetails = mediaDetailsRepository.findByApplicationApplicationId(applicationId).orElse(null);
            }

            // 2️⃣ Upload new file to S3 (inside folder platfrom_images/)
            String folderName = "frontend/platfrom_images/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String fileKey = folderName + fileName; // 👈 include folder name

            File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
            try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
                fos.write(file.getBytes());
            }

            // Upload file to S3 under the folder
            s3Client.putObject(new PutObjectRequest(bucketName, fileKey, convertedFile));

//            // Get S3 file URL
//            String fileUrl = s3Client.getUrl(bucketName, fileKey).toString();
            convertedFile.delete();

            // 3️⃣ Create or update Media + MediaDetails
            Media media;
            MediaDetails mediaDetails;

            if (existingMediaDetails != null) {
                media = existingMediaDetails.getMedia();
                if (media == null) media = new Media();

                // Delete old S3 file
//                if (media.getName() != null) {
//                    try {
//                        s3Client.deleteObject(bucketName, media.getName());
//                    } catch (Exception e) {
//                        System.out.println(e);
//                        log.warn("Could not delete old file from S3: {}", e.getMessage());
//                    }
//                }

                media.setName(fileName);
                media.setBaseImageUrl(fileName);
                media.setLastModifiedDate(LocalDateTime.now());
                media.setStatus("ACTIVE");
                mediaRepository.save(media);

                existingMediaDetails.setDescription(description);
                existingMediaDetails.setMediaFor(mediaFor);
                existingMediaDetails.setType(file.getContentType());
                existingMediaDetails.setLastModifiedDate(LocalDateTime.now());
                existingMediaDetails.setMedia(media);

                mediaDetailsRepository.save(existingMediaDetails);
                mediaDetails = existingMediaDetails;
            } else {
                media = new Media();
                media.setName(fileName);
                media.setBaseImageUrl(fileName);
                media.setCreatedDate(LocalDateTime.now());
                media.setStatus("ACTIVE");
                mediaRepository.save(media);

                mediaDetails = new MediaDetails();
                mediaDetails.setCreatedDate(LocalDateTime.now());
                mediaDetails.setDescription(description);
                mediaDetails.setMediaFor(mediaFor);
                mediaDetails.setType(file.getContentType());
                mediaDetails.setMedia(media);

                if (userId != null) {
                    mediaDetails.setUser(userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found")));
                } else if (applicationId != null) {
                    mediaDetails.setApplication(applicationRepository.findById(applicationId)
                            .orElseThrow(() -> new RuntimeException("Application not found")));
                }

                mediaDetailsRepository.save(mediaDetails);
            }

            return mediaDetails;

        } catch (Exception e) {
            log.error("Error uploading media: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading media: " + e.getMessage(), e);
        }
    }
}
