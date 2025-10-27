package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository;

import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.PurchaseDetailEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.PurchaseDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseDetailRepositoryJPA extends JpaRepository<PurchaseDetailEntity, PurchaseDetailId> {
}

