package com.camilo.ecommerce.application.usecase;

import com.camilo.ecommerce.domain.model.Payment;
import com.camilo.ecommerce.domain.repository.PaymentRepository;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PaymentRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PaymentResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentUseCaseTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentUseCase paymentUseCase;

    private Payment testPayment;
    private PaymentRequestDTO paymentRequestDTO;

    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setId(1);
        testPayment.setName("Credit Card");

        paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setName("Credit Card");
    }

    @Test
    void getAllPayments_ShouldReturnListOfPayments() {
        // Given
        List<Payment> payments = Arrays.asList(testPayment);
        when(paymentRepository.findAll()).thenReturn(payments);

        // When
        List<PaymentResponseDTO> result = paymentUseCase.getAllPayments();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void getPaymentById_WhenPaymentExists_ShouldReturnPayment() {
        // Given
        when(paymentRepository.findById(1)).thenReturn(Optional.of(testPayment));

        // When
        Optional<PaymentResponseDTO> result = paymentUseCase.getPaymentById(1);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Credit Card", result.get().getName());
    }

    @Test
    void createPayment_ShouldReturnCreatedPayment() {
        // Given
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        // When
        PaymentResponseDTO result = paymentUseCase.createPayment(paymentRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals("Credit Card", result.getName());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void updatePayment_WhenPaymentExists_ShouldReturnUpdatedPayment() {
        // Given
        PaymentRequestDTO updateDTO = new PaymentRequestDTO();
        updateDTO.setName("Debit Card");
        
        when(paymentRepository.findById(1)).thenReturn(Optional.of(testPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        // When
        Optional<PaymentResponseDTO> result = paymentUseCase.updatePayment(1, updateDTO);

        // Then
        assertTrue(result.isPresent());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void updatePayment_WhenPaymentNotExists_ShouldReturnEmpty() {
        // Given
        when(paymentRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<PaymentResponseDTO> result = paymentUseCase.updatePayment(999, paymentRequestDTO);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void deletePayment_ShouldCallRepository() {
        // When
        paymentUseCase.deletePayment(1);

        // Then
        verify(paymentRepository, times(1)).deleteById(1);
    }

    @Test
    void paymentExists_ShouldReturnTrueWhenExists() {
        // Given
        when(paymentRepository.existsById(1)).thenReturn(true);

        // When
        boolean result = paymentUseCase.paymentExists(1);

        // Then
        assertTrue(result);
    }
}

