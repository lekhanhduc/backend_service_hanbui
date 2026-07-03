package com.javabuilder.backendservice.dto.request;

import com.javabuilder.backendservice.entity.ProductImage;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequest(
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        List<ProductImage> images
) {
}
