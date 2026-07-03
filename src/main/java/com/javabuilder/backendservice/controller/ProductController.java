package com.javabuilder.backendservice.controller;

import com.javabuilder.backendservice.dto.request.CreateProductRequest;
import com.javabuilder.backendservice.dto.response.ApiResponse;
import com.javabuilder.backendservice.dto.response.CreateProductResponse;
import com.javabuilder.backendservice.dto.response.ProductDetailResponse;
import com.javabuilder.backendservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    ApiResponse<CreateProductResponse> createProduct(@AuthenticationPrincipal Jwt jwt,
                                                     @RequestBody CreateProductRequest request) {
        var userId = jwt.getSubject();
        var data = productService.createProduct(userId, request);
        return ApiResponse.<CreateProductResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Product created successfully")
                .data(data)
                .build();
    }

    @GetMapping
    ApiResponse<List<ProductDetailResponse>> getAllProducts() {
        var data = productService.getAllProducts();
        return ApiResponse.<List<ProductDetailResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Products retrieved successfully")
                .data(data)
                .build();
    }
}
