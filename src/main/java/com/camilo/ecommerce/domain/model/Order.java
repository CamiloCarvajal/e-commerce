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
public class Order {

    private int id;
    private String status;
    private LocalDateTime date;
    private double total_cost;
    private int total_items;
    private List<OrderDetail> orderDetails;

}
