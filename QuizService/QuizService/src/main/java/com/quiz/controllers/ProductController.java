package com.quiz.controllers;

import com.quiz.dto.ProductDTO;
import com.quiz.dto.ProductDTOResponse;
import com.quiz.dto.Response;
import com.quiz.services.ProductService;
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
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ResponseService responseService;

    @Operation(summary = "Create a list of Products")
    @PostMapping("/product/all")
    public ResponseEntity<Response<Void>> createAllProduct(@Valid @RequestBody List<ProductDTO> dtoList) {
        log.info("Request to create product list");
        productService.createAllProduct(dtoList);
        return responseService.success(HttpStatus.CREATED.value(), "Products created successfully", null, 0);
    }

    @Operation(summary = "Create a Product")
    @PostMapping("/product")
    public ResponseEntity<Response<ProductDTOResponse>> createProduct(@Valid @RequestBody ProductDTO dto) {
        log.info("Request to create product: {}", dto.getName());
        productService.createProduct(dto);
        return responseService.success(HttpStatus.CREATED.value(), "Product created successfully", null, 0);
    }

    @Operation(summary = "Get all Products")
    @GetMapping("/product")
    public ResponseEntity<Response<List<ProductDTOResponse>>> getAllProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<ProductDTOResponse> resultPage = productService.getAllProducts(PageRequest.of(page - 1, size));
        return responseService.success(HttpStatus.OK.value(), "Products fetched successfully",
                resultPage.getContent(), resultPage.getTotalElements());
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

    @Operation(summary = "Patch Product by ID")
    @PatchMapping("/product/{id}")
    public ResponseEntity<Response<ProductDTOResponse>> patchProduct(
            @PathVariable Long id, @RequestBody ProductDTO dto) {
        ProductDTOResponse updated = productService.patchProduct(id, dto);
        return responseService.success(HttpStatus.OK.value(), "Product partially updated successfully", updated, 1);
    }

    @Operation(summary = "Delete Product by ID")
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Response<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return responseService.success(HttpStatus.OK.value(), "Product deleted successfully", null, 0);
    }
}
