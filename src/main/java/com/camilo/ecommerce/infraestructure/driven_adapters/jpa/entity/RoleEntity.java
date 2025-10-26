package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "role")
public class RoleEntity {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserEntity> users;
}
