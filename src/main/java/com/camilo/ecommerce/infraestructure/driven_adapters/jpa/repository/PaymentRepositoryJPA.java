package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository;

import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepositoryJPA extends JpaRepository<PaymentEntity, Integer> {
}

