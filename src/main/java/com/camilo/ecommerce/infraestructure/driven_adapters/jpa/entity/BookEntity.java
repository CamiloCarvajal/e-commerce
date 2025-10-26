package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity;

import jakarta.persistence.Entity;

@Entity(name = "book")
public class BookEntity extends ProductEntity {

    private String author;
    private String language;
    private String topic;

}
