package com.qc.controllers;

import com.qc.dto.MediaDetailsDTO;
import com.qc.dto.MediaDetailsDTOResponse;
import com.qc.dto.Response;
import com.qc.exception.IllegalArgumentsException;
import com.qc.services.MediaDetailsService;
import com.qc.services.ResponseService;
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
@RequestMapping("/qc")
@Slf4j
public class MediaDetailsController {
    @Autowired
    private MediaDetailsService mediaDetailsService;

    @Autowired
    private ResponseService responseService;

    @Operation(summary = "Create multiple Media Details")
    @PostMapping("/mediaDetails/all")
    public ResponseEntity<Response<Void>> createAllMediaDetails(
            @Valid @RequestBody List<MediaDetailsDTO> dtoList,
            @RequestHeader("Authorization") String authHeader) {

        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentsException("Media Details list cannot be empty");
        }

        String token = authHeader.replace("Bearer ", "");
        mediaDetailsService.createAllMediaDetails(dtoList, token);
        return responseService.success(HttpStatus.CREATED.value(), "Media Details created successfully", null, 0);
    }

    @Operation(summary = "Create a Media Details entry")
    @PostMapping("/mediaDetails")
    public ResponseEntity<Response<Void>> createMediaDetails(
            @Valid @RequestBody MediaDetailsDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        mediaDetailsService.createMediaDetails(dto, token);
        return responseService.success(HttpStatus.CREATED.value(), "Media Details created successfully", null, 0);
    }

    @Operation(summary = "Get all Media Details")
    @GetMapping("/mediaDetails")
    public ResponseEntity<Response<List<MediaDetailsDTOResponse>>> getAllMediaDetails(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<MediaDetailsDTOResponse> resultPage = mediaDetailsService
                .getAllMediaDetails(PageRequest.of(page - 1, size));

        return responseService.success(HttpStatus.OK.value(), "Media Details fetched successfully",
                resultPage.getContent(), resultPage.getTotalElements());
    }

    @Operation(summary = "Get Media Details by ID")
    @GetMapping("/mediaDetails/{id}")
    public ResponseEntity<Response<MediaDetailsDTOResponse>> getMediaDetailsById(@PathVariable Long id) {
        MediaDetailsDTOResponse response = mediaDetailsService.getMediaDetailsById(id);
        return responseService.success(HttpStatus.OK.value(), "Media Details fetched successfully", response, 1);
    }

    @Operation(summary = "Update Media Details by ID")
    @PutMapping("/mediaDetails/{id}")
    public ResponseEntity<Response<MediaDetailsDTOResponse>> updateMediaDetails(
            @PathVariable Long id,
            @Valid @RequestBody MediaDetailsDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        MediaDetailsDTOResponse updated = mediaDetailsService.updateMediaDetails(id, dto, token);
        return responseService.success(HttpStatus.OK.value(), "Media Details updated successfully", updated, 1);
    }

    @Operation(summary = "Patch Media Details by ID")
    @PatchMapping("/mediaDetails/{id}")
    public ResponseEntity<Response<MediaDetailsDTOResponse>> patchMediaDetails(
            @PathVariable Long id,
            @RequestBody MediaDetailsDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        MediaDetailsDTOResponse updated = mediaDetailsService.patchMediaDetails(id, dto, token);
        return responseService.success(HttpStatus.OK.value(), "Media Details partially updated successfully", updated, 1);
    }

    @Operation(summary = "Delete Media Details by ID")
    @DeleteMapping("/mediaDetails/{id}")
    public ResponseEntity<Response<Void>> deleteMediaDetails(@PathVariable Long id) {
        mediaDetailsService.deleteMediaDetails(id);
        return responseService.success(HttpStatus.OK.value(), "Media Details deleted successfully", null, 0);
    }
}
