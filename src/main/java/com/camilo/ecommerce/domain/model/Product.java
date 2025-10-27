package com.camilo.ecommerce.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Product {

    private Integer id;
    private String name;
    private int number_pages;
    private double cost;
    private List<PurchaseDetail> purchaseDetails;

}
