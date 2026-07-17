package com.javabuilder.backendservice.service.impl;

import com.javabuilder.backendservice.dto.request.CreateProductRequest;
import com.javabuilder.backendservice.dto.response.CreateProductResponse;
import com.javabuilder.backendservice.dto.response.ProductDetailResponse;
import com.javabuilder.backendservice.entity.Product;
import com.javabuilder.backendservice.entity.User;
import com.javabuilder.backendservice.exception.CustomException;
import com.javabuilder.backendservice.exception.ErrorCode;
import com.javabuilder.backendservice.mapper.ProductMapper;
import com.javabuilder.backendservice.repository.ProductRepository;
import com.javabuilder.backendservice.repository.UserRepository;
import com.javabuilder.backendservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT-SERVICE")
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    @CacheEvict(value = "products", allEntries = true)
    @Override
    public CreateProductResponse createProduct(String userId, CreateProductRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Product product = productMapper.toProduct(request);
        product.setUser(user);

        productRepository.save(product);
        return productMapper.toCreateProductResponse(product);
    }

    @Cacheable(value = "products", key = "'all_product'")
    @Override
    public List<ProductDetailResponse> getAllProducts() {
        log.info("Getting all products");
        return productRepository.findAll().stream()
                .map(productMapper::toProductDetailResponse)
                .toList();
    }

}
