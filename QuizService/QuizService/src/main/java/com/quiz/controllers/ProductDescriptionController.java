package com.quiz.controllers;

import com.quiz.dto.ProductDescriptionDTO;
import com.quiz.dto.ProductDescriptionDTOResponse;
import com.quiz.dto.Response;
import com.quiz.exception.IllegalArgumentsException;
import com.quiz.services.ProductDescriptionService;
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
public class ProductDescriptionController {

    @Autowired
    private ProductDescriptionService productDescriptionService;

    @Autowired
    private ResponseService responseService;

    @Operation(summary = "Create a list of Product Descriptions")
    @PostMapping("/productDescription/all")
    public ResponseEntity<Response<Void>> createAllProductDescription(@Valid @RequestBody List<ProductDescriptionDTO> dtoList) {
        log.info("Request to create product description list");
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentsException("Product Description list cannot be empty");
        }
        productDescriptionService.createAllProductDescription(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Product Descriptions created successfully", null, 0);
    }

    @Operation(summary = "Create a Product Description")
    @PostMapping("/productDescription")
    public ResponseEntity<Response<ProductDescriptionDTOResponse>> createProductDescription(@Valid @RequestBody ProductDescriptionDTO dto) {
        log.info("Request to create product description for product ID: {}", dto.getProductId());
        productDescriptionService.createProductDescription(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Product Description created successfully", null, 0);
    }

    @Operation(summary = "Get all Product Descriptions")
    @GetMapping("/productDescription")
    public ResponseEntity<Response<List<ProductDescriptionDTOResponse>>> getAllProductDescription(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<ProductDescriptionDTOResponse> resultPage = productDescriptionService
                .getAllProductDescription(PageRequest.of(page - 1, size));

        return responseService.success(HttpStatus.OK.value(), "Product Descriptions fetched successfully",
                resultPage.getContent(), resultPage.getTotalElements());
    }

    @Operation(summary = "Get Product Description by ID")
    @GetMapping("/productDescription/{id}")
    public ResponseEntity<Response<ProductDescriptionDTOResponse>> getProductDescriptionById(@PathVariable Long id) {
        ProductDescriptionDTOResponse response = productDescriptionService.getProductDescriptionById(id);
        return responseService.success(HttpStatus.OK.value(), "Product Description fetched successfully", response, 1);
    }

    @Operation(summary = "Update Product Description by ID")
    @PutMapping("/productDescription/{id}")
    public ResponseEntity<Response<ProductDescriptionDTOResponse>> updateProductDescription(
            @PathVariable Long id, @Valid @RequestBody ProductDescriptionDTO dto) {

        ProductDescriptionDTOResponse updated = productDescriptionService.updateProductDescription(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Product Description updated successfully", updated, 1);
    }

    @Operation(summary = "Patch Product Description by ID")
    @PatchMapping("/productDescription/{id}")
    public ResponseEntity<Response<ProductDescriptionDTOResponse>> patchProductDescription(
            @PathVariable Long id, @RequestBody ProductDescriptionDTO dto) {

        ProductDescriptionDTOResponse updated = productDescriptionService.patchProductDescription(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Product Description partially updated successfully", updated, 1);
    }

    @Operation(summary = "Delete Product Description by ID")
    @DeleteMapping("/productDescription/{id}")
    public ResponseEntity<Response<Void>> deleteProductDescription(@PathVariable Long id) {
        productDescriptionService.deleteProductDescription(id);
        return responseService.success(HttpStatus.OK.value(), "Product Description deleted successfully", null, 0);
    }
}
