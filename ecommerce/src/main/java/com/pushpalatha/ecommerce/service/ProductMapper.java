package com.pushpalatha.ecommerce.service;

import com.pushpalatha.ecommerce.dto.ProductResponse;
import com.pushpalatha.ecommerce.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getImageUrl()
        );
    }
}
