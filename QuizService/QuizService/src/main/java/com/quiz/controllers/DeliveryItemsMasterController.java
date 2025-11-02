package com.quiz.controllers;

import com.quiz.dto.DeliveryItemsMasterDTO;
import com.quiz.dto.DeliveryItemsMasterDTOResponse;
import com.quiz.dto.Response;
import com.quiz.exception.IllegalArgumentsException;
import com.quiz.services.DeliveryItemsMasterService;
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
public class DeliveryItemsMasterController {

    @Autowired
    private DeliveryItemsMasterService deliveryItemsMasterService;

    @Autowired
    private ResponseService responseService;

    @Operation(summary = "Create multiple Delivery Items")
    @PostMapping("/deliveryItems/all")
    public ResponseEntity<Response<Void>> createAll(@Valid @RequestBody List<DeliveryItemsMasterDTO> dtoList) {
        log.info("Request to create delivery items list");
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentsException("Delivery Items list cannot be empty");
        }
        deliveryItemsMasterService.createAllDeliveryItems(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Delivery Items created successfully", null, 0);
    }

    @Operation(summary = "Create a Delivery Item")
    @PostMapping("/deliveryItems")
    public ResponseEntity<Response<DeliveryItemsMasterDTOResponse>> create(@Valid @RequestBody DeliveryItemsMasterDTO dto) {
        log.info("Request to create delivery item for challan ID: {}", dto.getChallanId());
        deliveryItemsMasterService.createDeliveryItem(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Delivery Item created successfully", null, 0);
    }

    @Operation(summary = "Get all Delivery Items with filters")
    @GetMapping("/deliveryItems")
    public ResponseEntity<Response<List<DeliveryItemsMasterDTOResponse>>> getAllDeliveryItems(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String serialNo,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) String hsnCode,
            @RequestParam(required = false) List<Long> challanIds,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<DeliveryItemsMasterDTOResponse> resultPage = deliveryItemsMasterService.getAllDeliveryItemsWithFilters(
                batchNo, name, productCode, orderNo, serialNo, unit, hsnCode, challanIds, pageable
        );

        return responseService.success(
                HttpStatus.OK.value(),
                "Delivery Items fetched successfully",
                resultPage.getContent(),
                resultPage.getTotalElements()
        );
    }

    @Operation(summary = "Get Delivery Item by ID")
    @GetMapping("/deliveryItems/{id}")
    public ResponseEntity<Response<DeliveryItemsMasterDTOResponse>> getById(@PathVariable Long id) {
        DeliveryItemsMasterDTOResponse response = deliveryItemsMasterService.getDeliveryItemById(id);
        return responseService.success(HttpStatus.OK.value(), "Delivery Item fetched successfully", response, 1);
    }

    @Operation(summary = "Update Delivery Item by ID")
    @PutMapping("/deliveryItems/{id}")
    public ResponseEntity<Response<DeliveryItemsMasterDTOResponse>> update(
            @PathVariable Long id, @Valid @RequestBody DeliveryItemsMasterDTO dto) {

        DeliveryItemsMasterDTOResponse updated = deliveryItemsMasterService.updateDeliveryItem(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Delivery Item updated successfully", updated, 1);
    }

    @Operation(summary = "Patch Delivery Item by ID")
    @PatchMapping("/deliveryItems/{id}")
    public ResponseEntity<Response<DeliveryItemsMasterDTOResponse>> patch(
            @PathVariable Long id, @RequestBody DeliveryItemsMasterDTO dto) {

        DeliveryItemsMasterDTOResponse updated = deliveryItemsMasterService.patchDeliveryItem(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Delivery Item partially updated successfully", updated, 1);
    }

    @Operation(summary = "Delete Delivery Item by ID")
    @DeleteMapping("/deliveryItems/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        deliveryItemsMasterService.deleteDeliveryItem(id);
        return responseService.success(HttpStatus.OK.value(), "Delivery Item deleted successfully", null, 0);
    }
}
