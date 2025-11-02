package com.quiz.controllers;

import com.quiz.dto.ProductMasterDTO;
import com.quiz.dto.ProductMasterDTOResponse;
import com.quiz.dto.Response;
import com.quiz.exception.IllegalArgumentsException;
import com.quiz.services.ProductMasterService;
import com.quiz.services.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ProductMasterController {

    private final ResponseService responseService;

    private final ProductMasterService productMasterService;

    public ProductMasterController(ResponseService responseService, ProductMasterService productMasterService) {
        this.responseService = responseService;
        this.productMasterService = productMasterService;
    }

    @Operation(summary = "Create a List of Product Masters")
    @PostMapping("/productMaster/all")
    public ResponseEntity<Response<Void>> createProductMasterAll(
            @Validated @RequestBody List<ProductMasterDTO> productMasterDTOList) {
        log.info("REST request to create product master list");
        if (productMasterDTOList == null || productMasterDTOList.isEmpty()) {
            throw new IllegalArgumentsException("Product Master list cannot be empty");
        }

        productMasterService.createAllProductMaster(productMasterDTOList);

        return responseService.success(HttpStatus.CREATED.value(), "Product Masters created successfully", null, 0);
    }
    @Operation(summary = "Create a Product Master")
    @PostMapping("/productMaster")
    public ResponseEntity<Response<ProductMasterDTOResponse>> createProductMaster(
            @Validated @RequestBody ProductMasterDTO productMasterDTO) {
        log.info("REST request to create product master : {}", productMasterDTO.getName());
        productMasterService.createProductMaster(productMasterDTO);

        return responseService.success(HttpStatus.CREATED.value(), "Product Masters created successfully", null, 0);
    }

    @Operation(summary = "Update Product Master by ID")
    @PutMapping("/productMaster/{id}")
    public ResponseEntity<Response<ProductMasterDTOResponse>> updateProductMaster(@PathVariable Long id,
                                                                                  @Validated @RequestBody ProductMasterDTO productMasterDTO) {
        log.info("Request to update Product Master with ID: {}", id);

        ProductMasterDTOResponse updatedProduct = productMasterService.updateProductMaster(id, productMasterDTO);

        return responseService.success(HttpStatus.OK.value(), "Product Master updated successfully", updatedProduct, 1);
    }

    @Operation(summary = "Partially update Product Master by ID")
    @PatchMapping("/productMaster/{id}")
    public ResponseEntity<Response<ProductMasterDTOResponse>> patchProductMaster(@PathVariable Long id,
                                                                                 @RequestBody ProductMasterDTO productMasterDTO) {
        log.info("Request to patch Product Master with ID: {}", id);

        ProductMasterDTOResponse updatedProduct = productMasterService.patchProductMaster(id, productMasterDTO);

        return responseService.success(HttpStatus.OK.value(), "Product Master updated successfully", updatedProduct, 1);
    }

    @Operation(summary = "List all Product Masters with optional filters, pagination, and sorting")
    @GetMapping("/productMaster")
    public ResponseEntity<Response<List<ProductMasterDTOResponse>>> getAllProductMasters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String serialNo,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String hsnCode,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) BigDecimal quantity,
            @RequestParam(required = false) Boolean isPublished,
            @RequestParam(required = false) Boolean status,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        log.info("Fetching Product Masters with filters: name={}, productCode={}, serialNo={}, orderNo={}, hsnCode={}, unit={}, price={}, quantity={}, isPublished={}, status={}",
                name, productCode, serialNo, orderNo, hsnCode, unit, price, quantity, isPublished, status);

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<ProductMasterDTOResponse> result = productMasterService.getAllProductMastersWithFilters(
                name, productCode, serialNo, orderNo, hsnCode, unit, price, quantity, isPublished, status, pageable);

        return responseService.success(HttpStatus.OK.value(),
                "Product Masters fetched successfully", result.getContent(), result.getTotalElements());
    }

    @Operation(summary = "Get Product Master by ID")
    @GetMapping("/productMaster/{id}")
    public ResponseEntity<Response<ProductMasterDTOResponse>> getProductMasterById(@PathVariable Long id) {
        log.info("Request to get Product Master with ID: {}", id);

        ProductMasterDTOResponse result = productMasterService.getProductMasterById(id);

        return responseService.success(HttpStatus.OK.value(), "Product Master fetched successfully", result, 1);
    }

    @Operation(summary = "Delete Product Master by ID")
    @DeleteMapping("/productMaster/{id}")
    public ResponseEntity<Response<Void>> deleteProductMaster(@PathVariable Long id) {
        log.info("Request to delete Product Master with ID: {}", id);

        productMasterService.deleteProductMaster(id);

        return responseService.success(HttpStatus.OK.value(), "Product Master deleted successfully", null, 0);
    }
}
