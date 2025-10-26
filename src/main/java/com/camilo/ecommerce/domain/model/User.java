package com.camilo.ecommerce.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String email;
    private String name;
    private String last_name;
    private String address;
    private String city;
    private String country;
    private String phone;
    private Role role;
    private List<Order> orders;

}
