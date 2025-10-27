package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "purchase_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDetailEntity {

    @EmbeddedId
    private PurchaseDetailId purchaseDetailId;

    @ManyToOne
    @MapsId("purchaseId")
    @JoinColumn(name = "purchase_id")
    private PurchaseEntity purchase;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private double cost;
    private int items;
}
