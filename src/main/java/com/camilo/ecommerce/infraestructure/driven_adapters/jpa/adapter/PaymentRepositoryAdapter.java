package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.domain.model.Payment;
import com.camilo.ecommerce.domain.repository.PaymentRepository;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.PaymentEntity;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.repository.PaymentRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PaymentRepositoryAdapter implements PaymentRepository {

    @Autowired
    private PaymentRepositoryJPA paymentRepositoryJPA;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public List<Payment> findAll() {
        return paymentRepositoryJPA.findAll().stream()
                .map(paymentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Payment> findById(Integer id) {
        return paymentRepositoryJPA.findById(id).map(paymentMapper::toDomain);
    }

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = paymentMapper.toEntity(payment);
        PaymentEntity saved = paymentRepositoryJPA.save(entity);
        return paymentMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Integer id) {
        paymentRepositoryJPA.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return paymentRepositoryJPA.existsById(id);
    }
}

