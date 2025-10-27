package com.camilo.ecommerce.infraestructure.entry_points.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseResponseDTO {
    private Integer id;
    private String status;
    private LocalDateTime date;
    private Double total_cost;
    private Integer total_items;
    private List<PurchaseDetailResponseDTO> purchaseDetails;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Double getTotal_cost() { return total_cost; }
    public void setTotal_cost(Double total_cost) { this.total_cost = total_cost; }

    public Integer getTotal_items() { return total_items; }
    public void setTotal_items(Integer total_items) { this.total_items = total_items; }

    public List<PurchaseDetailResponseDTO> getPurchaseDetails() { return purchaseDetails; }
    public void setPurchaseDetails(List<PurchaseDetailResponseDTO> purchaseDetails) { this.purchaseDetails = purchaseDetails; }
}

