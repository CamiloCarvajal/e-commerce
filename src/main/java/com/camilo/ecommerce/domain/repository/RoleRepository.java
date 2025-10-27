package com.camilo.ecommerce.domain.repository;

import com.camilo.ecommerce.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    List<Role> findAll();
    Optional<Role> findById(Integer id);
    Role save(Role role);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}

