package com.camilo.ecommerce.infraestructure.driven_adapters.jpa.adapter;

import com.camilo.ecommerce.domain.model.Payment;
import com.camilo.ecommerce.infraestructure.driven_adapters.jpa.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentEntity toEntity(Payment payment) {
        if (payment == null) return null;

        PaymentEntity entity = new PaymentEntity();
        entity.setId(payment.getId());
        entity.setName(payment.getName());
        return entity;
    }

    public Payment toDomain(PaymentEntity entity) {
        if (entity == null) return null;

        Payment payment = new Payment();
        payment.setId(entity.getId());
        payment.setName(entity.getName());
        return payment;
    }
}

