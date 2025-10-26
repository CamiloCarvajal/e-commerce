package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "notebook")
public class NotebookEntity extends ProductEntity {

    @Column(name = "column_type")
    private String lineType;
}
