package com.camilo.ecommerce.application.usecase;

import com.camilo.ecommerce.domain.model.*;
import com.camilo.ecommerce.domain.repository.*;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PurchaseDetailRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PurchaseRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PurchaseResponseDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PurchaseUpdateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class PurchaseUseCaseTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PurchaseDetailRepository purchaseDetailRepository;

    @InjectMocks
    private PurchaseUseCase purchaseUseCase;

    private User testUser;
    private Book testBook;
    private Payment testPayment;
    private Purchase testPurchase;
    private PurchaseRequestDTO purchaseRequestDTO;

    @BeforeEach
    void setUp() {
        // Setup role
        Role role = new Role();
        role.setId(1);
        role.setName("CUSTOMER");

        // Setup user
        testUser = new User();
        testUser.setEmail("user@example.com");
        testUser.setName("John");
        testUser.setLast_name("Doe");
        testUser.setRole(role);

        // Setup book product
        testBook = new Book();
        testBook.setId(1);
        testBook.setName("Clean Code");
        testBook.setNumber_pages(464);
        testBook.setCost(49.99);

        // Setup payment
        testPayment = new Payment();
        testPayment.setId(1);
        testPayment.setName("Credit Card");

        // Setup purchase
        testPurchase = new Purchase();
        testPurchase.setId(1);
        testPurchase.setStatus("PENDING");
        testPurchase.setDate(LocalDateTime.now());
        testPurchase.setTotal_cost(49.99);
        testPurchase.setTotal_items(1);
        testPurchase.setUser(testUser);
        testPurchase.setPayment(testPayment);

        // Setup purchase request DTO
        purchaseRequestDTO = new PurchaseRequestDTO();
        purchaseRequestDTO.setUserEmail("user@example.com");
        purchaseRequestDTO.setPaymentId(1);
        
        PurchaseDetailRequestDTO detailDTO = new PurchaseDetailRequestDTO();
        detailDTO.setProductId(1);
        detailDTO.setItems(1);
        purchaseRequestDTO.setPurchaseDetails(Arrays.asList(detailDTO));
    }

    @Test
    void getAllPurchases_ShouldReturnListOfPurchases() {
        
        when(purchaseRepository.findAll()).thenReturn(Arrays.asList(testPurchase));
        
        List<PurchaseResponseDTO> result = purchaseUseCase.getAllPurchases();

        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(purchaseRepository, times(1)).findAll();
    }

    @Test
    void getPurchaseById_WhenPurchaseExists_ShouldReturnPurchase() {
        
        when(purchaseRepository.findById(1)).thenReturn(Optional.of(testPurchase));
        
        Optional<PurchaseResponseDTO> result = purchaseUseCase.getPurchaseById(1);
        
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(purchaseRepository, times(1)).findById(1);
    }

    @Test
    void createPurchase_ShouldCalculateTotalsAndSave() {
        
        when(userRepository.findById("user@example.com")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(testBook));
        when(paymentRepository.findById(1)).thenReturn(Optional.of(testPayment));
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(testPurchase);
        when(purchaseDetailRepository.save(any(PurchaseDetail.class))).thenReturn(new PurchaseDetail());

        PurchaseResponseDTO result = purchaseUseCase.createPurchase(purchaseRequestDTO);
        
        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
        verify(purchaseDetailRepository, atLeastOnce()).save(any(PurchaseDetail.class));
    }

    @Test
    void createPurchase_WhenUserNotFound_ShouldThrowException() {
        
        lenient().when(userRepository.findById("user@example.com")).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> {
            purchaseUseCase.createPurchase(purchaseRequestDTO);
        });
    }

    @Test
    void createPurchase_WhenProductNotFound_ShouldThrowException() {
        
        lenient().when(userRepository.findById("user@example.com")).thenReturn(Optional.of(testUser));
        lenient().when(productRepository.findById(1)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> {
            purchaseUseCase.createPurchase(purchaseRequestDTO);
        });
    }

    @Test
    void updatePurchase_WhenPurchaseExists_ShouldUpdateStatus() {
        
        PurchaseUpdateRequestDTO updateDTO = new PurchaseUpdateRequestDTO();
        updateDTO.setStatus("PAID");
        
        when(purchaseRepository.findById(1)).thenReturn(Optional.of(testPurchase));
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(testPurchase);

        Optional<PurchaseResponseDTO> result = purchaseUseCase.updatePurchase(1, updateDTO);
        
        assertTrue(result.isPresent());
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    @Test
    void updatePurchase_WhenPurchaseNotExists_ShouldReturnEmpty() {
        
        PurchaseUpdateRequestDTO updateDTO = new PurchaseUpdateRequestDTO();
        updateDTO.setStatus("PAID");
        when(purchaseRepository.findById(999)).thenReturn(Optional.empty());

        
        Optional<PurchaseResponseDTO> result = purchaseUseCase.updatePurchase(999, updateDTO);
        assertTrue(result.isEmpty());
    }

    @Test
    void deletePurchase_ShouldCallRepository() {
        
        purchaseUseCase.deletePurchase(1);
        verify(purchaseRepository, times(1)).deleteById(1);
    }

    @Test
    void purchaseExists_ShouldReturnTrueWhenExists() {
        
        when(purchaseRepository.existsById(1)).thenReturn(true);
        boolean result = purchaseUseCase.purchaseExists(1);
        
        assertTrue(result);
    }

    @Test
    void getPurchasesByUserEmail_ShouldReturnUserPurchases() {
        
        when(purchaseRepository.findByUserEmail("user@example.com"))
                .thenReturn(Arrays.asList(testPurchase));

        List<PurchaseResponseDTO> result = purchaseUseCase.getPurchasesByUserEmail("user@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(purchaseRepository, times(1)).findByUserEmail("user@example.com");
    }

    @Test
    void getPurchasesByUserEmailAndStatus_ShouldReturnFilteredPurchases() {
        
        when(purchaseRepository.findByUserEmailAndStatus("user@example.com", "PENDING"))
                .thenReturn(Arrays.asList(testPurchase));

        
        List<PurchaseResponseDTO> result = purchaseUseCase.getPurchasesByUserEmailAndStatus("user@example.com", "PENDING");

        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
    }
}

