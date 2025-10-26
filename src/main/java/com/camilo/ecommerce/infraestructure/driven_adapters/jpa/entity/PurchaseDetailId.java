package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity;

import java.io.Serializable;

public class PurchaseDetailId implements Serializable {

    private int productId;
    private int purchaseId;

    public PurchaseDetailId() {
    }

    public PurchaseDetailId(int productId, int purchaseId) {
        this.productId = productId;
        this.purchaseId = purchaseId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }
}
