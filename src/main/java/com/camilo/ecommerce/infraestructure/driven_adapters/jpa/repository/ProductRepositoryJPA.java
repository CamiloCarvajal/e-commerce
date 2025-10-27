package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository;

import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositoryJPA extends JpaRepository<ProductEntity, Integer> {
}

