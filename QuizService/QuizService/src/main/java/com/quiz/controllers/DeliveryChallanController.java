package com.quiz.controllers;

import com.quiz.dto.DeliveryChallanDTO;
import com.quiz.dto.DeliveryChallanDTOResponse;
import com.quiz.dto.Response;
import com.quiz.exception.IllegalArgumentsException;
import com.quiz.services.DeliveryChallanService;
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
@RequestMapping("/api")
@Slf4j
public class DeliveryChallanController {
    @Autowired
    private DeliveryChallanService deliveryChallanService;

    @Autowired
    private ResponseService responseService;

    @Operation(summary = "Create a list of Delivery Challans")
    @PostMapping("/deliveryChallan/all")
    public ResponseEntity<Response<Void>> createAllDeliveryChallan(@Valid @RequestBody List<DeliveryChallanDTO> dtoList) {
        log.info("Request to create delivery challan list");
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentsException("Delivery Challan list cannot be empty");
        }
        deliveryChallanService.createAllDeliveryChallan(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Delivery Challans created successfully", null, 0);
    }

    @Operation(summary = "Create a Delivery Challan")
    @PostMapping("/deliveryChallan")
    public ResponseEntity<Response<DeliveryChallanDTOResponse>> createDeliveryChallan(@Valid @RequestBody DeliveryChallanDTO dto) {
        log.info("Request to create delivery challan: {}", dto.getName());
        deliveryChallanService.createDeliveryChallan(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Delivery Challan created successfully", null, 0);
    }

    @Operation(summary = "Get all Delivery Challans with filters")
    @GetMapping("/deliveryChallan")
    public ResponseEntity<Response<List<DeliveryChallanDTOResponse>>> getAllDeliveryChallan(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String descriptions,
            @RequestParam(required = false) Boolean status,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<DeliveryChallanDTOResponse> resultPage =
                deliveryChallanService.getAllDeliveryChallanWithFilters(
                        name, descriptions, status, pageable
                );

        return responseService.success(
                HttpStatus.OK.value(),
                "Delivery Challans fetched successfully",
                resultPage.getContent(),
                resultPage.getTotalElements()
        );
    }

    @Operation(summary = "Get Delivery Challan by ID")
    @GetMapping("/deliveryChallan/{id}")
    public ResponseEntity<Response<DeliveryChallanDTOResponse>> getDeliveryChallanById(@PathVariable Long id) {
        DeliveryChallanDTOResponse response = deliveryChallanService.getDeliveryChallanById(id);
        return responseService.success(HttpStatus.OK.value(), "Delivery Challan fetched successfully", response, 1);
    }

    @Operation(summary = "Update Delivery Challan by ID")
    @PutMapping("/deliveryChallan/{id}")
    public ResponseEntity<Response<DeliveryChallanDTOResponse>> updateDeliveryChallan(
            @PathVariable Long id, @Valid @RequestBody DeliveryChallanDTO dto) {

        DeliveryChallanDTOResponse updated = deliveryChallanService.updateDeliveryChallan(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Delivery Challan updated successfully", updated, 1);
    }

    @Operation(summary = "Patch Delivery Challan by ID")
    @PatchMapping("/deliveryChallan/{id}")
    public ResponseEntity<Response<DeliveryChallanDTOResponse>> patchDeliveryChallan(
            @PathVariable Long id, @RequestBody DeliveryChallanDTO dto) {

        DeliveryChallanDTOResponse updated = deliveryChallanService.patchDeliveryChallan(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Delivery Challan partially updated successfully", updated, 1);
    }

    @Operation(summary = "Delete Delivery Challan by ID")
    @DeleteMapping("/deliveryChallan/{id}")
    public ResponseEntity<Response<Void>> deleteDeliveryChallan(@PathVariable Long id) {
        deliveryChallanService.deleteDeliveryChallan(id);
        return responseService.success(HttpStatus.OK.value(), "Delivery Challan deleted successfully", null, 0);
    }
}
