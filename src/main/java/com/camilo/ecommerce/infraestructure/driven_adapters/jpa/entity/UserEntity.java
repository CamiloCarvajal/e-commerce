package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//User is a reserved word
@Entity(name = "e_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private String email;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    private String address;
    private String city;
    private String country;
    private String phone;
    private String password;
    @ManyToOne
    private RoleEntity role;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PurchaseEntity> purchases;

}
