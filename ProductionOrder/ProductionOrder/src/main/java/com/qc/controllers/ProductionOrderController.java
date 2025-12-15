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
@RequestMapping("/prodorder")
@Slf4j
public class ProductionOrderController {
    @Autowired
    private ResponseService responseService;

    @Autowired
    private ProductionOrderService productionOrderService;

    @PostMapping("/productionOrder/all")
    public ResponseEntity<Response<Void>> createAll(
            @Validated @RequestBody List<ProductionOrderDTO> dtoList) {

        productionOrderService.createAllProductionOrders(dtoList);
        return responseService.success(201, "Production Orders created successfully", null, 0);
    }

    @PostMapping("/productionOrder")
    public ResponseEntity<Response<Void>> create(
            @Validated @RequestBody ProductionOrderDTO dto) {

        productionOrderService.createProductionOrder(dto);
        return responseService.success(201, "Production Order created successfully", null, 0);
    }

    @GetMapping("/productionOrder")
    public ResponseEntity<Response<List<ProductionOrderDTOResponse>>> getAll(
            @RequestParam(required = false) String productionOrderNo,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String currentWorkCenter,
            @RequestParam(required = false) String activityNumber,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) Long priority,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String productDescription,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Pageable pageable = PageRequest.of(page - 1, size,
                sortDir.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        Page<ProductionOrderDTOResponse> result =
                productionOrderService.getAll(productionOrderNo, batchNo, currentWorkCenter,
                        activityNumber, operation, priority,     productCode,
                        productDescription,pageable);

        return responseService.success(200, "Production Orders fetched successfully",
                result.getContent(), result.getTotalElements());
    }

    @GetMapping("/productionOrder/{id}")
    public ResponseEntity<Response<ProductionOrderDTOResponse>> getById(@PathVariable Long id) {

        ProductionOrderDTOResponse res = productionOrderService.getById(id);

        return responseService.success(200, "Production Order fetched successfully", res, 1);
    }

    @PutMapping("/productionOrder/{id}")
    public ResponseEntity<Response<ProductionOrderDTOResponse>> update(
            @PathVariable Long id, @Validated @RequestBody ProductionOrderDTO dto) {

        ProductionOrderDTOResponse res = productionOrderService.update(id, dto);

        return responseService.success(200, "Production Order updated successfully", res, 1);
    }

    @PatchMapping("/productionOrder/{id}")
    public ResponseEntity<Response<ProductionOrderDTOResponse>> patch(
            @PathVariable Long id, @RequestBody ProductionOrderDTO dto) {

        ProductionOrderDTOResponse res = productionOrderService.patch(id, dto);

        return responseService.success(200, "Production Order patched successfully", res, 1);
    }

    @DeleteMapping("/productionOrder/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {

        productionOrderService.delete(id);
        return responseService.success(200, "Production Order deleted successfully", null, 0);
    }

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

            List<ProductionOrder> saved = productionOrderService.uploadProductionOrdersFromExcel(file, token);

            return ResponseEntity.ok(
                    new ResponceData("success", 200, "Production orders uploaded", saved, saved.size())
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponceData("error", 500, e.getMessage(), null, 0));
        }
    }

    @GetMapping("/distinct/currentWorkCenters")
    public ResponseEntity<Response<List<String>>> getDistinctCurrentWorkCenters() {

        List<String> workCenters = productionOrderService.getDistinctCurrentWorkCenters();

        return ResponseEntity.ok(
                new Response<>(
                        "success",
                        200,
                        "Distinct current work centers fetched successfully",
                        workCenters,
                        workCenters.size()
                )
        );
    }
}
