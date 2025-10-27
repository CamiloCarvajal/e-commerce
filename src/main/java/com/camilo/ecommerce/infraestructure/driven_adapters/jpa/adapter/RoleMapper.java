package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.domain.model.Role;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleEntity toEntity(Role role) {
        if (role == null) return null;

        RoleEntity entity = new RoleEntity();
        entity.setId(role.getId());
        entity.setName(role.getName());
        return entity;
    }

    public Role toDomain(RoleEntity entity) {
        if (entity == null) return null;

        Role role = new Role();
        role.setId(entity.getId());
        role.setName(entity.getName());
        return role;
    }
}

