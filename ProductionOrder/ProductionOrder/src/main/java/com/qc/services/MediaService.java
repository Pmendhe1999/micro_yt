package com.qc.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.qc.config.IdentityClient;
import com.qc.dto.MediaDTO;
import com.qc.dto.MediaDTOResponse;
import com.qc.entities.Media;
import com.qc.entities.MediaDetails;
import com.qc.entities.MediaMaster;
import com.qc.exception.ResourceNotFoundException;
import com.qc.mapper.MediaMapper;
import com.qc.repositories.MediaDetailsRepository;
import com.qc.repositories.MediaMasterRepository;
import com.qc.repositories.MediaRepository;
import com.qc.repositories.ProductMasterRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

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


    @Autowired
    private  AmazonS3 s3Client;

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


    public void deleteMediaWithDetails(Long mediaId) {
        // ✅ Check if media exists
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found with ID: " + mediaId));

        // ✅ Delete all MediaDetails linked to this media
        mediaDetailsRepository.deleteByMediaId(mediaId);

        // ✅ Delete Media itself
        mediaRepository.delete(media);
    }



    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    private MediaDetailsRepository mediaDetailsRepository;

    @Autowired
    private MediaMasterRepository mediaMasterRepository;

    @Autowired
    private ProductMasterRepository productMasterRepository;

    @Transactional
    public MediaMaster uploadProductMedia(Long productId, MultipartFile file, String description, String mediaFor,String token) {
        try {
            // 1️⃣ Prepare S3 upload details
            String folderName = "frontend/platfrom_images/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String fileKey = folderName + fileName;

            // Convert file to temp file
            File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
            try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
                fos.write(file.getBytes());
            }

            // Upload file to S3
            s3Client.putObject(new PutObjectRequest(bucketName, fileKey, convertedFile));
            convertedFile.delete();

            Map<String, Object> authData = identityClient.validateToken(token);
            String uploadedBy = (String) authData.get("username");

            // 2️⃣ Create new Media entry
            Media media = new Media();
            media.setName(fileName);
            media.setBaseImageUrl(fileKey);
            media.setDescription(description);
            media.setType(file.getContentType());
            media.setStatus("ACTIVE");
            media.setUploadedBy(uploadedBy);
            mediaRepository.save(media);

            // 3️⃣ Create new MediaDetails entry
            MediaDetails mediaDetails = new MediaDetails();
            mediaDetails.setMedia(media);
            mediaDetails.setDescription(description);
            mediaDetails.setMediaFor(mediaFor);
            mediaDetails.setType(file.getContentType());
            mediaDetails.setName(fileName);
            mediaDetails.setUploadedBy(uploadedBy);
            mediaDetails.setProductMaster(productMasterRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found")));
            mediaDetailsRepository.save(mediaDetails);

            // 4️⃣ Always create a new MediaMaster entry (even if existing exists)
            MediaMaster mediaMaster = new MediaMaster();
            mediaMaster.setName(fileName);
            mediaMaster.setBaseImageUrl(fileKey);
            mediaMaster.setDescription(description);
            mediaMaster.setType(file.getContentType());
            mediaMaster.setStatus("ACTIVE");

            mediaMaster.setProductMaster(productMasterRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found")));

            mediaMasterRepository.save(mediaMaster);

            return mediaMaster;

        } catch (Exception e) {
            log.error("Error uploading product media: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading product media: " + e.getMessage(), e);
        }
    }
    // ✅ Get all media by productMasterId
    public List<MediaMaster> getAllMediaByProductMasterId(Long productId) {
        List<MediaMaster> mediaList = mediaMasterRepository.findAllByProductMasterId(productId);
        if (mediaList.isEmpty()) {
            throw new RuntimeException("No media found for ProductMaster ID: " + productId);
        }
        return mediaList;
    }

    @Transactional
    public Map<String, Object> uploadQualitativeMedia(
            MultipartFile file, String description, String mediaFor, String token) {

        try {
            String folderName = "frontend/qualitative_images/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String fileKey = folderName + fileName;

            // Convert file
            File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
            try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
                fos.write(file.getBytes());
            }

            // Upload to S3
            s3Client.putObject(new PutObjectRequest(bucketName, fileKey, convertedFile));
            convertedFile.delete();

            Map<String, Object> authData = identityClient.validateToken(token);
            String uploadedBy = (String) authData.get("username");

            // 1️⃣ Create Media
            Media media = new Media();
            media.setName(fileName);
            media.setBaseImageUrl(fileKey);
            media.setDescription(description);
            media.setType(file.getContentType());
            media.setStatus("ACTIVE");
            media.setUploadedBy(uploadedBy);
            mediaRepository.save(media);

            // 2️⃣ Create MediaDetails
            MediaDetails mediaDetails = new MediaDetails();
            mediaDetails.setMedia(media);
            mediaDetails.setName(fileName);
            mediaDetails.setDescription(description);
            mediaDetails.setMediaFor(mediaFor);
            mediaDetails.setType(file.getContentType());
            mediaDetails.setUploadedBy(uploadedBy);
            mediaDetailsRepository.save(mediaDetails);

            // 3️⃣ Response
            Map<String, Object> result = new HashMap<>();
            result.put("media", media);
            result.put("mediaDetails", mediaDetails);

            return result;

        } catch (Exception e) {
            log.error("Error uploading qualitative media: {}", e.getMessage(), e);
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }
}
