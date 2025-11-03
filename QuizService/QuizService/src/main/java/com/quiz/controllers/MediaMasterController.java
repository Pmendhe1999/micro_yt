package com.quiz.controllers;

import com.quiz.dto.MediaMasterDTO;
import com.quiz.dto.MediaMasterDTOResponse;
import com.quiz.dto.Response;
import com.quiz.services.MediaMasterService;
import com.quiz.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
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
public class MediaMasterController {
    @Autowired
    private MediaMasterService mediaMasterService;

    @Autowired
    private ResponseService responseService;

    @Operation(summary = "Create a Media")
    @PostMapping("/mediaMaster")
    public ResponseEntity<Response<Void>> createMedia(
            @Valid @RequestBody MediaMasterDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        mediaMasterService.createMedia(dto, token);
        return responseService.success(HttpStatus.CREATED.value(), "Media created successfully", null, 0);
    }

    @Operation(summary = "Create multiple Media")
    @PostMapping("/mediaMaster/all")
    public ResponseEntity<Response<Void>> createAllMedia(
            @Valid @RequestBody List<MediaMasterDTO> dtoList,
            @RequestHeader("Authorization") String authHeader) {

        if (dtoList == null || dtoList.isEmpty())
            throw new IllegalArgumentException("Media list cannot be empty");

        String token = authHeader.replace("Bearer ", "");
        mediaMasterService.createAllMedia(dtoList, token);
        return responseService.success(HttpStatus.CREATED.value(), "Media list created successfully", null, 0);
    }

    @Operation(summary = "Get all Medias")
    @GetMapping("/mediaMaster")
    public ResponseEntity<Response<List<MediaMasterDTOResponse>>> getAllMedia(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<MediaMasterDTOResponse> resultPage = mediaMasterService.getAllMedia(PageRequest.of(page - 1, size));
        return responseService.success(HttpStatus.OK.value(), "Media fetched successfully",
                resultPage.getContent(), resultPage.getTotalElements());
    }

    @Operation(summary = "Get Media by ID")
    @GetMapping("/mediaMaster/{id}")
    public ResponseEntity<Response<MediaMasterDTOResponse>> getMediaById(@PathVariable Long id) {
        MediaMasterDTOResponse response = mediaMasterService.getMediaById(id);
        return responseService.success(HttpStatus.OK.value(), "Media fetched successfully", response, 1);
    }

    @Operation(summary = "Update Media by ID")
    @PutMapping("/mediaMaster/{id}")
    public ResponseEntity<Response<MediaMasterDTOResponse>> updateMedia(
            @PathVariable Long id,
            @Valid @RequestBody MediaMasterDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        MediaMasterDTOResponse updated = mediaMasterService.updateMedia(id, dto, token);
        return responseService.success(HttpStatus.OK.value(), "Media updated successfully", updated, 1);
    }

    @Operation(summary = "Patch Media by ID")
    @PatchMapping("/mediaMaster/{id}")
    public ResponseEntity<Response<MediaMasterDTOResponse>> patchMedia(
            @PathVariable Long id,
            @RequestBody MediaMasterDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        MediaMasterDTOResponse updated = mediaMasterService.patchMedia(id, dto, token);
        return responseService.success(HttpStatus.OK.value(), "Media partially updated successfully", updated, 1);
    }

    @Operation(summary = "Delete Media by ID")
    @DeleteMapping("/mediaMaster/{id}")
    public ResponseEntity<Response<Void>> deleteMedia(@PathVariable Long id) {
        mediaMasterService.deleteMedia(id);
        return responseService.success(HttpStatus.OK.value(), "Media deleted successfully", null, 0);
    }
}
