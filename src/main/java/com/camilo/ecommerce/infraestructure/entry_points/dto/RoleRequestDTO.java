package com.camilo.ecommerce.infraestructure.entry_points.dto;

import jakarta.validation.constraints.NotBlank;

public class RoleRequestDTO {
    
    @NotBlank(message = "Name is required")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

