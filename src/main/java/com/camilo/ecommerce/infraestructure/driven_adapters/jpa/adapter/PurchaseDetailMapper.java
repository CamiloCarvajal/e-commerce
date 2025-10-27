package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.domain.model.Purchase;
import com.camilo.ecommerce.domain.model.PurchaseDetail;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseDetailMapper {

    @Autowired
    private ProductMapper productMapper;

    public PurchaseDetailEntity toEntity(PurchaseDetail purchaseDetail) {
        if (purchaseDetail == null) return null;

        PurchaseDetailEntity entity = new PurchaseDetailEntity();
        
        PurchaseDetailId id = new PurchaseDetailId();
        if (purchaseDetail.getProduct() != null) {
            id.setProductId(purchaseDetail.getProduct().getId());
        }
        if (purchaseDetail.getPurchase() != null) {
            id.setPurchaseId(purchaseDetail.getPurchase().getId());
        }
        
        entity.setPurchaseDetailId(id);
        entity.setCost(purchaseDetail.getCost());
        entity.setItems(purchaseDetail.getItems());
        
        if (purchaseDetail.getProduct() != null) {
            entity.setProduct(productMapper.toEntity(purchaseDetail.getProduct()));
        }
        
        return entity;
    }

    public PurchaseDetail toDomain(PurchaseDetailEntity entity) {
        if (entity == null) return null;

        PurchaseDetail purchaseDetail = new PurchaseDetail();
        purchaseDetail.setCost(entity.getCost());
        purchaseDetail.setItems(entity.getItems());
        
        if (entity.getProduct() != null) {
            purchaseDetail.setProduct(productMapper.toDomain(entity.getProduct()));
        }
        
        if (entity.getPurchase() != null) {
            Purchase purchase = new Purchase();
            purchase.setId(entity.getPurchase().getId());
            purchase.setStatus(entity.getPurchase().getStatus());
            purchase.setDate(entity.getPurchase().getDate());
            purchase.setTotal_cost(entity.getPurchase().getTotalCost());
            purchase.setTotal_items(entity.getPurchase().getTotalItems());
            purchaseDetail.setPurchase(purchase);
        }
        
        return purchaseDetail;
    }
}

