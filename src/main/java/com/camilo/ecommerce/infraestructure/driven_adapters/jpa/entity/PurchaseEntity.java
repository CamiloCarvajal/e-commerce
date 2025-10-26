package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "purchase")
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String status;
    private LocalDateTime date;
    @Column(name = "total_cost")
    private double totalCost;
    @Column(name = "total_items")
    private int totalItems;

    @OneToOne
    private UserEntity user;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<PurchaseDetailEntity> purchaseDetails;

    @ManyToOne
    private PaymentEntity payment;

}
