package com.quiz.controllers;

import com.quiz.dto.DeliveryChallanMasterDTO;
import com.quiz.dto.DeliveryChallanMasterDTOResponse;
import com.quiz.dto.Response;
import com.quiz.exception.IllegalArgumentsException;
import com.quiz.services.DeliveryChallanMasterService;
import com.quiz.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qc")
@Slf4j
public class DeliveryChallanMasterController {

    @Autowired
    private DeliveryChallanMasterService service;

    @Autowired
    private ResponseService responseService;

    @Operation(summary = "Create a list of Delivery Challans")
    @PostMapping("/deliveryChallanMaster/all")
    public ResponseEntity<Response<Void>> createAllDeliveryChallan(@Valid @RequestBody List<DeliveryChallanMasterDTO> dtoList) {
        log.info("Request to create delivery challan list");
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentsException("Delivery Challan list cannot be empty");
        }
        service.createAllDeliveryChallanMaster(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Delivery Challans created successfully", null, 0);
    }

    @Operation(summary = "Create a Delivery Challan")
    @PostMapping("/deliveryChallanMaster")
    public ResponseEntity<Response<Void>> createDeliveryChallan(@Valid @RequestBody DeliveryChallanMasterDTO dto) {
        log.info("Request to create delivery challan: {}", dto.getName());
        service.createDeliveryChallanMaster(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Delivery Challan created successfully", null, 0);
    }

    @Operation(summary = "Get all Delivery Challans with filters")
    @GetMapping("/deliveryChallanMaster")
    public ResponseEntity<Response<List<DeliveryChallanMasterDTOResponse>>> getAllDeliveryChallan(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String descriptions,
            @RequestParam(required = false) Boolean status,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<DeliveryChallanMasterDTOResponse> resultPage =
                service.getAllDeliveryChallanMaster(name, descriptions, status, pageable);

        return responseService.success(HttpStatus.OK.value(),
                "Delivery Challans fetched successfully",
                resultPage.getContent(), resultPage.getTotalElements());
    }

    @Operation(summary = "Get Delivery Challan by ID")
    @GetMapping("/deliveryChallanMaster/{id}")
    public ResponseEntity<Response<DeliveryChallanMasterDTOResponse>> getDeliveryChallanById(@PathVariable Long id) {
        DeliveryChallanMasterDTOResponse response = service.getDeliveryChallanMasterById(id);
        return responseService.success(HttpStatus.OK.value(), "Delivery Challan fetched successfully", response, 1);
    }

    @Operation(summary = "Update Delivery Challan by ID")
    @PutMapping("/deliveryChallanMaster/{id}")
    public ResponseEntity<Response<DeliveryChallanMasterDTOResponse>> updateDeliveryChallan(
            @PathVariable Long id, @Valid @RequestBody DeliveryChallanMasterDTO dto) {

        DeliveryChallanMasterDTOResponse updated = service.updateDeliveryChallanMaster(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Delivery Challan updated successfully", updated, 1);
    }

    @Operation(summary = "Patch Delivery Challan by ID")
    @PatchMapping("/deliveryChallanMaster/{id}")
    public ResponseEntity<Response<DeliveryChallanMasterDTOResponse>> patchDeliveryChallan(
            @PathVariable Long id, @RequestBody DeliveryChallanMasterDTO dto) {

        DeliveryChallanMasterDTOResponse updated = service.patchDeliveryChallanMaster(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Delivery Challan partially updated successfully", updated, 1);
    }

    @Operation(summary = "Delete Delivery Challan by ID")
    @DeleteMapping("/deliveryChallanMaster/{id}")
    public ResponseEntity<Response<Void>> deleteDeliveryChallan(@PathVariable Long id) {
        service.deleteDeliveryChallanMaster(id);
        return responseService.success(HttpStatus.OK.value(), "Delivery Challan deleted successfully", null, 0);
    }
}
