package com.camilo.ecommerce.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {

    private int id;
    private String status;
    private LocalDateTime date;
    private double total_cost;
    private int total_items;
    private User user;
    private Payment payment;
    private List<PurchaseDetail> purchaseDetails;

}
