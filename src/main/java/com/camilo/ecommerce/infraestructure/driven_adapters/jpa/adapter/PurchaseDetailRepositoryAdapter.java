package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.domain.model.PurchaseDetail;
import com.camilo.ecommerce.domain.repository.PurchaseDetailRepository;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.PurchaseDetailEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository.PurchaseDetailRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PurchaseDetailRepositoryAdapter implements PurchaseDetailRepository {

    @Autowired
    private PurchaseDetailRepositoryJPA purchaseDetailRepositoryJPA;

    @Autowired
    private PurchaseDetailMapper purchaseDetailMapper;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public PurchaseDetail save(PurchaseDetail purchaseDetail) {
        PurchaseDetailEntity entity = purchaseDetailMapper.toEntity(purchaseDetail);
        
        // Set the purchase entity reference
        if (purchaseDetail.getPurchase() != null) {
            entity.setPurchase(purchaseMapper.toEntity(purchaseDetail.getPurchase()));
        }
        
        PurchaseDetailEntity saved = purchaseDetailRepositoryJPA.save(entity);
        return purchaseDetailMapper.toDomain(saved);
    }
}
