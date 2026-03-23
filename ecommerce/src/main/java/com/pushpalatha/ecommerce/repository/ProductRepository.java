package com.pushpalatha.ecommerce.repository;

import com.pushpalatha.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
