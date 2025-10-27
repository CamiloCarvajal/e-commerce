package com.camilo.ecommerce.domain.repository;

import com.camilo.ecommerce.domain.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    List<Payment> findAll();
    Optional<Payment> findById(Integer id);
    Payment save(Payment payment);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}

