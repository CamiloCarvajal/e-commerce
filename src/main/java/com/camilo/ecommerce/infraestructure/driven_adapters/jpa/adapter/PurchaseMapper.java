package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.domain.model.Purchase;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.PurchaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PurchaseDetailMapper purchaseDetailMapper;

    public PurchaseEntity toEntity(Purchase purchase) {
        if (purchase == null) return null;

        PurchaseEntity entity = new PurchaseEntity();
        entity.setId(purchase.getId());
        entity.setStatus(purchase.getStatus());
        entity.setDate(purchase.getDate());
        entity.setTotalCost(purchase.getTotal_cost());
        entity.setTotalItems(purchase.getTotal_items());
        entity.setUser(purchase.getUser() != null ? userMapper.toEntity(purchase.getUser()) : null);
        entity.setPayment(purchase.getPayment() != null ? paymentMapper.toEntity(purchase.getPayment()) : null);
        if (purchase.getPurchaseDetails() != null) {
            entity.setPurchaseDetails(purchase.getPurchaseDetails().stream()
                    .map(purchaseDetailMapper::toEntity)
                    .collect(java.util.stream.Collectors.toList()));
        }
        
        return entity;
    }

    public Purchase toDomain(PurchaseEntity entity) {
        if (entity == null) return null;

        Purchase purchase = new Purchase();
        purchase.setId(entity.getId());
        purchase.setStatus(entity.getStatus());
        purchase.setDate(entity.getDate());
        purchase.setTotal_cost(entity.getTotalCost());
        purchase.setTotal_items(entity.getTotalItems());
        purchase.setUser(entity.getUser() != null ? userMapper.toDomain(entity.getUser()) : null);
        purchase.setPayment(entity.getPayment() != null ? paymentMapper.toDomain(entity.getPayment()) : null);
        if (entity.getPurchaseDetails() != null) {
            purchase.setPurchaseDetails(entity.getPurchaseDetails().stream()
                    .map(purchaseDetailMapper::toDomain)
                    .collect(java.util.stream.Collectors.toList()));
        }
        
        return purchase;
    }
}

