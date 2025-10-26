package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int number_pages;
    private double cost;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<PurchaseDetailEntity> purchaseDetails;

}
