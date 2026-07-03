package com.javabuilder.backendservice.dto.response;

import com.javabuilder.backendservice.entity.ProductImage;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record CreateProductResponse(
        String id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        List<ProductImage> images,
        Instant createdAt
) {
}
