package com.camilo.ecommerce.infraestructure.entry_points.dto;

public class ProductResponseDTO {
    private Integer id;
    private String name;
    private Integer number_pages;
    private Double cost;
    
    // For Book
    private String author;
    private String language;
    private String topic;
    
    // For Notebook
    private String lineType;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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
}

