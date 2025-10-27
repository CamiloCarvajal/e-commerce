package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.domain.model.Role;
import com.camilo.ecommerce.domain.repository.RoleRepository;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.RoleEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository.RoleRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RoleRepositoryAdapter implements RoleRepository {

    @Autowired
    private RoleRepositoryJPA roleRepositoryJPA;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findAll() {
        return roleRepositoryJPA.findAll().stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Role> findById(Integer id) {
        return roleRepositoryJPA.findById(id).map(roleMapper::toDomain);
    }

    @Override
    public Role save(Role role) {
        RoleEntity entity = roleMapper.toEntity(role);
        RoleEntity saved = roleRepositoryJPA.save(entity);
        return roleMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Integer id) {
        roleRepositoryJPA.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return roleRepositoryJPA.existsById(id);
    }
}

