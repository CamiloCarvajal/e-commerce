package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository;

import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.NotebookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotebookRepositoryJPA extends JpaRepository<NotebookEntity, Integer> {
    List<NotebookEntity> findByLineTypeContainingIgnoreCase(String lineType);
}

