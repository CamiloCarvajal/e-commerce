package com.camilo.ecommerce.infraestructure.entry_points.dto;

public class PurchaseDetailResponseDTO {
    private ProductResponseDTO product;
    private Double cost;
    private Integer items;

    public ProductResponseDTO getProduct() { return product; }
    public void setProduct(ProductResponseDTO product) { this.product = product; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public Integer getItems() { return items; }
    public void setItems(Integer items) { this.items = items; }
}

