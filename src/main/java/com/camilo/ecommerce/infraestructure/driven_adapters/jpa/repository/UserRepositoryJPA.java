package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository;

import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJPA extends JpaRepository<UserEntity, String> {
}

