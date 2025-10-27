package com.camilo.ecommerce.domain.repository;

import com.camilo.ecommerce.domain.model.Purchase;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {
    List<Purchase> findAll();
    Optional<Purchase> findById(Integer id);
    Purchase save(Purchase purchase);
    void deleteById(Integer id);
    boolean existsById(Integer id);
    List<Purchase> findByUserEmail(String email);
    List<Purchase> findByUserEmailAndStatus(String email, String status);
}

