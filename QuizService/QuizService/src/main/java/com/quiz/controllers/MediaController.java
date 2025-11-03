package com.quiz.controllers;

import com.quiz.dto.MediaDTO;
import com.quiz.dto.MediaDTOResponse;
import com.quiz.dto.Response;
import com.quiz.exception.IllegalArgumentsException;
import com.quiz.services.MediaService;
import com.quiz.services.ResponseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class MediaController {
    @Autowired
    private MediaService mediaService;

    @Autowired
    private ResponseService responseService;

    @PostMapping("/media/all")
    public ResponseEntity<Response<Void>> createAllMedia(
            @Valid @RequestBody List<MediaDTO> dtoList,
            @RequestHeader("Authorization") String authHeader) {

        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentsException("Media list cannot be empty");
        }
        String token = authHeader.replace("Bearer ", "");
        mediaService.createAllMedia(dtoList, token);
        return responseService.success(HttpStatus.CREATED.value(), "Media records created successfully", null, 0);
    }

    @PostMapping("/media")
    public ResponseEntity<Response<MediaDTOResponse>> createMedia(
            @Valid @RequestBody MediaDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        log.info("Request to create media by user token");
        String token = authHeader.replace("Bearer ", "");
        MediaDTOResponse created = mediaService.createMedia(dto, token);
        return responseService.success(HttpStatus.CREATED.value(), "Media created successfully", created, 1);
    }

    @GetMapping("/media")
    public ResponseEntity<Response<List<MediaDTOResponse>>> getAllMedia(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<MediaDTOResponse> resultPage = mediaService.getAllMedia(PageRequest.of(page - 1, size));
        return responseService.success(HttpStatus.OK.value(), "Media fetched successfully",
                resultPage.getContent(), resultPage.getTotalElements());
    }

    @GetMapping("/media/{id}")
    public ResponseEntity<Response<MediaDTOResponse>> getMediaById(@PathVariable Long id) {
        MediaDTOResponse response = mediaService.getMediaById(id);
        return responseService.success(HttpStatus.OK.value(), "Media fetched successfully", response, 1);
    }

    @PutMapping("/media/{id}")
    public ResponseEntity<Response<MediaDTOResponse>> updateMedia(
            @PathVariable Long id,
            @Valid @RequestBody MediaDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        MediaDTOResponse updated = mediaService.updateMedia(id, dto, token);
        return responseService.success(HttpStatus.OK.value(), "Media updated successfully", updated, 1);
    }

    @PatchMapping("/media/{id}")
    public ResponseEntity<Response<MediaDTOResponse>> patchMedia(
            @PathVariable Long id,
            @RequestBody MediaDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        MediaDTOResponse updated = mediaService.patchMedia(id, dto, token);
        return responseService.success(HttpStatus.OK.value(), "Media partially updated successfully", updated, 1);
    }

    @DeleteMapping("/media/{id}")
    public ResponseEntity<Response<Void>> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return responseService.success(HttpStatus.OK.value(), "Media deleted successfully", null, 0);
    }
}
