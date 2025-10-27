package com.camilo.ecommerce.infraestructure.entry_points.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class ProductRequestDTO {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @Min(value = 1, message = "Number of pages must be at least 1")
    @NotNull(message = "Number of pages is required")
    private Integer number_pages;
    
    @Min(value = 0, message = "Cost must be positive")
    @NotNull(message = "Cost is required")
    private Double cost;
    
    // For Book
    private String author;
    private String language;
    private String topic;
    
    // For Notebook
    private String lineType;
    
    private String type; // "book" or "notebook"

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getNumber_pages() { return number_pages; }
    public void setNumber_pages(Integer number_pages) { this.number_pages = number_pages; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getLineType() { return lineType; }
    public void setLineType(String lineType) { this.lineType = lineType; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

