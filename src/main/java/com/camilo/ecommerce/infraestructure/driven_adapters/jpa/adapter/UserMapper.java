package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.application.port.Crypto;
import com.camilo.ecommerce.domain.model.User;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private Crypto crypto;

    @Autowired
    private RoleMapper roleMapper;

    public UserEntity toEntity(User user) {
        if (user == null) return null;

        UserEntity entity = new UserEntity();
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());
        
        // Encrypt sensitive data
        if (user.getLast_name() != null) {
            entity.setLastName(crypto.encode(user.getLast_name()));
        }
        if (user.getAddress() != null) {
            entity.setAddress(crypto.encode(user.getAddress()));
        }
        if (user.getCity() != null) {
            entity.setCity(crypto.encode(user.getCity()));
        }
        if (user.getCountry() != null) {
            entity.setCountry(crypto.encode(user.getCountry()));
        }
        if (user.getPhone() != null) {
            entity.setPhone(crypto.encode(user.getPhone()));
        }
        
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole() != null ? roleMapper.toEntity(user.getRole()) : null);
        
        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        User user = new User();
        user.setEmail(entity.getEmail());
        user.setName(entity.getName());
        
        // Decrypt sensitive data
        if (entity.getLastName() != null) {
            user.setLast_name(crypto.decode(entity.getLastName()));
        }
        if (entity.getAddress() != null) {
            user.setAddress(crypto.decode(entity.getAddress()));
        }
        if (entity.getCity() != null) {
            user.setCity(crypto.decode(entity.getCity()));
        }
        if (entity.getCountry() != null) {
            user.setCountry(crypto.decode(entity.getCountry()));
        }
        if (entity.getPhone() != null) {
            user.setPhone(crypto.decode(entity.getPhone()));
        }
        
        user.setPassword(entity.getPassword());
        user.setRole(entity.getRole() != null ? roleMapper.toDomain(entity.getRole()) : null);
        
        return user;
    }
}

