package com.camilo.ecommerce.application.usecase;

import com.camilo.ecommerce.domain.model.Payment;
import com.camilo.ecommerce.domain.repository.PaymentRepository;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PaymentRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentUseCase {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<PaymentResponseDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<PaymentResponseDTO> getPaymentById(Integer id) {
        return paymentRepository.findById(id).map(this::convertToResponseDTO);
    }

    public PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO) {
        Payment payment = new Payment();
        payment.setName(requestDTO.getName());
        Payment saved = paymentRepository.save(payment);
        return convertToResponseDTO(saved);
    }

    public Optional<PaymentResponseDTO> updatePayment(Integer id, PaymentRequestDTO requestDTO) {
        return paymentRepository.findById(id).map(existingPayment -> {
            existingPayment.setName(requestDTO.getName());
            Payment updated = paymentRepository.save(existingPayment);
            return convertToResponseDTO(updated);
        });
    }

    public void deletePayment(Integer id) {
        paymentRepository.deleteById(id);
    }

    public boolean paymentExists(Integer id) {
        return paymentRepository.existsById(id);
    }

    private PaymentResponseDTO convertToResponseDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setName(payment.getName());
        return dto;
    }
}

