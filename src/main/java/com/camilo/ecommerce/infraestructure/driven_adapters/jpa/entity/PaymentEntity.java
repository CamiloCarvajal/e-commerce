package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseEntity> purchases;
}
