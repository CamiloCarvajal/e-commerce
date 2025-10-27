package com.camilo.ecommerce.infraestructure.entry_points.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PurchaseRequestDTO {
    
    @NotBlank(message = "User email is required")
    private String userEmail;
    
    @NotBlank(message = "Status is required")
    private String status;
    
    @NotNull(message = "Payment ID is required")
    private Integer paymentId;
    
    @NotEmpty(message = "At least one product is required")
    private List<PurchaseDetailRequestDTO> purchaseDetails;

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }

    public List<PurchaseDetailRequestDTO> getPurchaseDetails() { return purchaseDetails; }
    public void setPurchaseDetails(List<PurchaseDetailRequestDTO> purchaseDetails) { this.purchaseDetails = purchaseDetails; }
}

