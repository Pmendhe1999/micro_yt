package com.qc.controllers;

import com.qc.dto.ProductDTO;
import com.qc.dto.ProductDTOResponse;
import com.qc.dto.Response;
import com.qc.services.ProductService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/qc")
@Slf4j
public class ProductController {

    @Autowired private ProductService productService;
    @Autowired private ResponseService responseService;

    @Operation(summary = "Create Product")
    @PostMapping("/product")
    public ResponseEntity<Response<ProductDTOResponse>> createProduct(@Valid @RequestBody ProductDTO dto) {
        log.info("Creating Product: {}", dto.getName());
        ProductDTOResponse created = productService.createProduct(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Product created successfully", created, 1);
    }

    @Operation(summary = "Get All Products with Filters")
    @GetMapping("/product")
    public ResponseEntity<Response<List<ProductDTOResponse>>> getAllProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String serialNo,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String hsnCode,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) Long inStockQuantity,
            @RequestParam(required = false) LocalDate mfgDate,
            @RequestParam(required = false) LocalDate expDate,
            @RequestParam(required = false) Boolean isPublished,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Long deliveryChallanId,
            @RequestParam(required = false) Long deliveryItemId,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<ProductDTOResponse> result = productService.getAllProductsWithFilters(
                name, productCode, serialNo, orderNo, batchNo, hsnCode, unit, price,
                inStockQuantity, mfgDate, expDate, isPublished, status,
                deliveryChallanId, deliveryItemId, pageable);

        return responseService.success(HttpStatus.OK.value(),
                "Products fetched successfully", result.getContent(), result.getTotalElements());
    }

    @Operation(summary = "Get Product by ID")
    @GetMapping("/product/{id}")
    public ResponseEntity<Response<ProductDTOResponse>> getProductById(@PathVariable Long id) {
        ProductDTOResponse response = productService.getProductById(id);
        return responseService.success(HttpStatus.OK.value(), "Product fetched successfully", response, 1);
    }

    @Operation(summary = "Update Product by ID")
    @PutMapping("/product/{id}")
    public ResponseEntity<Response<ProductDTOResponse>> updateProduct(
            @PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        ProductDTOResponse updated = productService.updateProduct(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Product updated successfully", updated, 1);
    }

    @Operation(summary = "Delete Product by ID")
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Response<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return responseService.success(HttpStatus.OK.value(), "Product deleted successfully", null, 0);
    }
}
