package com.javabuilder.backendservice.mapper;

import com.javabuilder.backendservice.dto.request.CreateProductRequest;
import com.javabuilder.backendservice.dto.response.CreateProductResponse;
import com.javabuilder.backendservice.dto.response.ProductDetailResponse;
import com.javabuilder.backendservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    Product toProduct(CreateProductRequest request);

    CreateProductResponse toCreateProductResponse(Product product);

    ProductDetailResponse toProductDetailResponse(Product product);
}
