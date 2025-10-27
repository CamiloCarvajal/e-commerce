package com.camilo.ecommerce.infraestructure.entry_points.dto;

import java.util.List;

public class UserResponseDTO {
    private String email;
    private String name;
    private String last_name;
    private String address;
    private String city;
    private String country;
    private String phone;
    private Integer roleId;
    private List<PurchaseResponseDTO> purchases;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }

    public List<PurchaseResponseDTO> getPurchases() { return purchases; }
    public void setPurchases(List<PurchaseResponseDTO> purchases) { this.purchases = purchases; }
}

