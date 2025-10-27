package com.camilo.ecommerce.domain.repository;

import com.camilo.ecommerce.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(Integer id);
    Product save(Product product);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
