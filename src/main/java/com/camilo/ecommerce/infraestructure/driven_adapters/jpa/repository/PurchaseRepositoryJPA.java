package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository;

import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepositoryJPA extends JpaRepository<PurchaseEntity, Integer> {
    List<PurchaseEntity> findByUserEmail(String email);
    List<PurchaseEntity> findByUserEmailAndStatus(String email, String status);
}

