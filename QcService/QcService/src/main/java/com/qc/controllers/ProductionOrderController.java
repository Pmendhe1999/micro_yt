package com.qc.controllers;

import com.qc.dto.ProductionOrderDTO;
import com.qc.dto.ProductionOrderDTOResponse;
import com.qc.dto.ResponceData;
import com.qc.dto.Response;
import com.qc.entities.ProductionOrder;
import com.qc.services.ProductionOrderService;
import com.qc.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/qc")
@Slf4j
public class ProductionOrderController {

    @Autowired
    private ResponseService responseService;

    @Autowired
    private ProductionOrderService productionOrderService;

    @Operation(summary = "Create a List of Production Orders")
    @PostMapping("/productionOrder/all")
    public ResponseEntity<Response<Void>> createAll(
            @Validated @RequestBody List<ProductionOrderDTO> dtoList) {
        log.info("Request to create list of Production Orders");
        productionOrderService.createAllProductionOrders(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Production Orders created successfully", null, 0);
    }

    @Operation(summary = "Create a single Production Order")
    @PostMapping("/productionOrder")
    public ResponseEntity<Response<Void>> create(
            @Validated @RequestBody ProductionOrderDTO dto) {
        log.info("Request to create Production Order: {}", dto.getName());
        productionOrderService.createProductionOrder(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Production Order created successfully", null, 0);
    }

    @Operation(summary = "Update Production Order by ID")
    @PutMapping("/productionOrder/{id}")
    public ResponseEntity<Response<ProductionOrderDTOResponse>> update(
            @PathVariable Long id, @Validated @RequestBody ProductionOrderDTO dto) {

        log.info("Request to update Production Order with ID: {}", id);
        ProductionOrderDTOResponse updated = productionOrderService.updateProductionOrder(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Production Order updated successfully", updated, 1);
    }

    @Operation(summary = "Partially update Production Order by ID")
    @PatchMapping("/productionOrder/{id}")
    public ResponseEntity<Response<ProductionOrderDTOResponse>> patch(
            @PathVariable Long id, @RequestBody ProductionOrderDTO dto) {

        log.info("Request to patch Production Order with ID: {}", id);
        ProductionOrderDTOResponse updated = productionOrderService.patchProductionOrder(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Production Order updated successfully", updated, 1);
    }

    @Operation(summary = "Get all Production Orders with filters, pagination, and sorting")
    @GetMapping("/productionOrder")
    public ResponseEntity<Response<List<ProductionOrderDTOResponse>>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Long priority,
            @RequestParam(required = false) String productionOrderNo,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("Fetching Production Orders with filters: name={}, description={}, status={}, priority={}, productionOrderNo={}",
                name, description, status, priority, productionOrderNo);

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<ProductionOrderDTOResponse> result =
                productionOrderService.getAllProductionOrdersWithFilters(name, description, status, priority, productionOrderNo, pageable);

        return responseService.success(HttpStatus.OK.value(), "Production Orders fetched successfully",
                result.getContent(), result.getTotalElements());
    }

    @Operation(summary = "Get Production Order by ID")
    @GetMapping("/productionOrder/{id}")
    public ResponseEntity<Response<ProductionOrderDTOResponse>> getById(@PathVariable Long id) {
        log.info("Fetching Production Order with ID: {}", id);
        ProductionOrderDTOResponse result = productionOrderService.getProductionOrderById(id);
        return responseService.success(HttpStatus.OK.value(), "Production Order fetched successfully", result, 1);
    }

    @Operation(summary = "Delete Production Order by ID")
    @DeleteMapping("/productionOrder/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
        log.info("Deleting Production Order with ID: {}", id);
        productionOrderService.deleteProductionOrder(id);
        return responseService.success(HttpStatus.OK.value(), "Production Order deleted successfully", null, 0);
    }

    @Operation(summary = "Upload Production Orders via Excel")
    @PostMapping("/productionOrderUpload")
    public ResponseEntity<ResponceData> uploadProductionOrders(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");

            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ResponceData("fail", 400, "File is empty", null, 0));
            }

            List<ProductionOrder> savedOrders = productionOrderService.uploadProductionOrdersFromExcel(file, token);

            return ResponseEntity.ok(new ResponceData(
                    "success", 200, "Production Orders uploaded successfully", savedOrders, savedOrders.size()));

        } catch (Exception e) {
            log.error("Error uploading Production Orders: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, "Unexpected error: " + e.getMessage(), null, 0));
        }
    }
}
