package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.domain.model.Purchase;
import com.camilo.ecommerce.domain.model.PurchaseDetail;
import com.camilo.ecommerce.domain.repository.PurchaseRepository;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.PurchaseEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository.PurchaseRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PurchaseRepositoryAdapter implements PurchaseRepository {

    @Autowired
    private PurchaseRepositoryJPA purchaseRepositoryJPA;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public List<Purchase> findAll() {
        return purchaseRepositoryJPA.findAll().stream()
                .map(purchaseMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Purchase> findById(Integer id) {
        return purchaseRepositoryJPA.findById(id).map(purchaseMapper::toDomain);
    }

    @Override
    public Purchase save(Purchase purchase) {
        // Don't include purchase details when saving Purchase
        // Details will be saved separately after Purchase is persisted
        List<PurchaseDetail> tempDetails = purchase.getPurchaseDetails();
        purchase.setPurchaseDetails(null);
        
        PurchaseEntity entity = purchaseMapper.toEntity(purchase);
        PurchaseEntity saved = purchaseRepositoryJPA.save(entity);
        
        // Restore details for the returned domain object
        purchase.setPurchaseDetails(tempDetails);
        
        return purchaseMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Integer id) {
        purchaseRepositoryJPA.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return purchaseRepositoryJPA.existsById(id);
    }

    @Override
    public List<Purchase> findByUserEmail(String email) {
        return purchaseRepositoryJPA.findByUserEmail(email).stream()
                .map(purchaseMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Purchase> findByUserEmailAndStatus(String email, String status) {
        return purchaseRepositoryJPA.findByUserEmailAndStatus(email, status).stream()
                .map(purchaseMapper::toDomain)
                .collect(Collectors.toList());
    }
}

