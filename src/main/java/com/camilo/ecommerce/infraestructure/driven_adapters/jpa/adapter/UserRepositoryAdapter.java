package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.application.port.Crypto;
import com.camilo.ecommerce.domain.model.User;
import com.camilo.ecommerce.domain.repository.UserRepository;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.UserEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository.UserRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    @Autowired
    private UserRepositoryJPA userRepositoryJPA;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Crypto crypto;

    @Override
    public List<User> findAll() {
        return userRepositoryJPA.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(String email) {
        return userRepositoryJPA.findById(email).map(userMapper::toDomain);
    }

    @Override
    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(crypto.encodePassword(user.getPassword()));
        }
        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = userRepositoryJPA.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public void deleteById(String email) {
        userRepositoryJPA.deleteById(email);
    }

    @Override
    public boolean existsById(String email) {
        return userRepositoryJPA.existsById(email);
    }
}

