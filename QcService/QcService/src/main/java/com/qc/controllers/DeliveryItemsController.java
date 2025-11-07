package com.qc.controllers;

import com.qc.dto.DeliveryItemsDTO;
import com.qc.dto.DeliveryItemsDTOResponse;
import com.qc.dto.Response;
import com.qc.exception.IllegalArgumentsException;
import com.qc.services.DeliveryItemsService;
import com.qc.services.ResponseService;
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
public class DeliveryItemsController {
    @Autowired
    private DeliveryItemsService deliveryItemsService;

    @Autowired
    private ResponseService responseService;

    @Operation(summary = "Create multiple Delivery Items")
    @PostMapping("/deliveryItems/all")
    public ResponseEntity<Response<Void>> createAll(@Valid @RequestBody List<DeliveryItemsDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentsException("Delivery Items list cannot be empty");
        }
        deliveryItemsService.createAll(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Delivery Items created successfully", null, 0);
    }

    @Operation(summary = "Create a Delivery Item")
    @PostMapping("/deliveryItems")
    public ResponseEntity<Response<DeliveryItemsDTOResponse>> create(@Valid @RequestBody DeliveryItemsDTO dto) {
        DeliveryItemsDTOResponse response = deliveryItemsService.create(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Delivery Item created successfully", response, 1);
    }

    @Operation(summary = "Get all Delivery Items with filters")
    @GetMapping("/deliveryItems")
    public ResponseEntity<Response<List<DeliveryItemsDTOResponse>>> getAll(
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

        Page<DeliveryItemsDTOResponse> resultPage = deliveryItemsService.getAll(
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
    public ResponseEntity<Response<DeliveryItemsDTOResponse>> getById(@PathVariable Long id) {
        DeliveryItemsDTOResponse response = deliveryItemsService.getById(id);
        return responseService.success(HttpStatus.OK.value(), "Delivery Item fetched successfully", response, 1);
    }

    @Operation(summary = "Update Delivery Item by ID")
    @PutMapping("/deliveryItems/{id}")
    public ResponseEntity<Response<DeliveryItemsDTOResponse>> update(
            @PathVariable Long id, @Valid @RequestBody DeliveryItemsDTO dto) {
        DeliveryItemsDTOResponse updated = deliveryItemsService.update(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Delivery Item updated successfully", updated, 1);
    }

    @Operation(summary = "Patch Delivery Item by ID")
    @PatchMapping("/deliveryItems/{id}")
    public ResponseEntity<Response<DeliveryItemsDTOResponse>> patch(
            @PathVariable Long id, @RequestBody DeliveryItemsDTO dto) {
        DeliveryItemsDTOResponse updated = deliveryItemsService.patch(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Delivery Item partially updated successfully", updated, 1);
    }

    @Operation(summary = "Delete Delivery Item by ID")
    @DeleteMapping("/deliveryItems/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        deliveryItemsService.delete(id);
        return responseService.success(HttpStatus.OK.value(), "Delivery Item deleted successfully", null, 0);
    }
}
