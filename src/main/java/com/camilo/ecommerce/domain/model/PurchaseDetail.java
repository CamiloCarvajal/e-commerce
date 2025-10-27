package com.camilo.ecommerce.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDetail {

    private Purchase purchase;
    private Product product;
    private double cost;
    private int items;
}
