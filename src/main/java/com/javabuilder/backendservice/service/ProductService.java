package com.javabuilder.backendservice.service;

import com.javabuilder.backendservice.dto.request.CreateProductRequest;
import com.javabuilder.backendservice.dto.response.CreateProductResponse;
import com.javabuilder.backendservice.dto.response.ProductDetailResponse;

import java.util.List;

public interface ProductService {

    CreateProductResponse createProduct(String userId, CreateProductRequest request);

    List<ProductDetailResponse> getAllProducts();
}
