package com.camilo.ecommerce.infraestructure.entry_points.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PurchaseDetailRequestDTO {
    
    @NotNull(message = "Product ID is required")
    private Integer productId;
    
    @Min(value = 1, message = "Items must be at least 1")
    @NotNull(message = "Items is required")
    private Integer items;

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getItems() { return items; }
    public void setItems(Integer items) { this.items = items; }
}

