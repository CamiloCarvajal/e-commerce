package com.camilo.ecommerce.domain.repository;

import com.camilo.ecommerce.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(String email);
    User save(User user);
    void deleteById(String email);
    boolean existsById(String email);
}
