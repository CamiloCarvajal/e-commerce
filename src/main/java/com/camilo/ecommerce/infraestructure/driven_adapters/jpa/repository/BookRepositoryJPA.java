package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository;

import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepositoryJPA extends JpaRepository<BookEntity, Integer> {
    List<BookEntity> findByAuthorContainingIgnoreCase(String author);
}

