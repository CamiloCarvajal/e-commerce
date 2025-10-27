package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "purchase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "user_email")
    private UserEntity user;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<PurchaseDetailEntity> purchaseDetails;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

}
