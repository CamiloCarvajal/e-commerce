package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "notebook")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotebookEntity extends ProductEntity {

    @Column(name = "column_type")
    private String lineType;
}
