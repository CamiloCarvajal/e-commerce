package com.camilo.ecommerce.domain.repository;

import com.camilo.ecommerce.domain.model.PurchaseDetail;

public interface PurchaseDetailRepository {
    PurchaseDetail save(PurchaseDetail purchaseDetail);
}
