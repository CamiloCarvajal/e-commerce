package com.camilo.ecommerce.application.usecase;

import com.camilo.ecommerce.domain.model.*;
import com.camilo.ecommerce.domain.repository.*;
import com.camilo.ecommerce.infraestructure.entry_points.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseUseCase {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PurchaseDetailRepository purchaseDetailRepository;

    public List<PurchaseResponseDTO> getAllPurchases() {
        List<Purchase> purchases = purchaseRepository.findAll();
        return purchases.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<PurchaseResponseDTO> getPurchaseById(Integer id) {
        return purchaseRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Transactional
    public PurchaseResponseDTO createPurchase(PurchaseRequestDTO requestDTO) {
        Purchase purchase = new Purchase();
        purchase.setStatus("PENDING");
        purchase.setDate(LocalDateTime.now());
        
        // Set user
        User user = userRepository.findById(requestDTO.getUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        purchase.setUser(user);
        
        // Set payment
        Payment payment = paymentRepository.findById(requestDTO.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        purchase.setPayment(payment);
        
        // Create purchase details and calculate totals (business logic in domain layer)
        List<PurchaseDetail> details = new ArrayList<>();
        double totalCost = 0;
        int totalItems = 0;
        
        for (PurchaseDetailRequestDTO detailDTO : requestDTO.getPurchaseDetails()) {
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            PurchaseDetail detail = new PurchaseDetail();
            detail.setProduct(product);
            detail.setItems(detailDTO.getItems());
            detail.setCost(product.getCost() * detailDTO.getItems());
            detail.setPurchase(purchase);
            
            details.add(detail);
            
            // Calculate totals
            totalCost += product.getCost() * detailDTO.getItems();
            totalItems += detailDTO.getItems();
        }
        
        // Set calculated totals
        purchase.setTotal_cost(totalCost);
        purchase.setTotal_items(totalItems);
        purchase.setPurchaseDetails(details);

        // Step 1: Save Purchase first (without details to get ID)
        Purchase saved = purchaseRepository.save(purchase);
        
        // Step 2: Save PurchaseDetails separately after Purchase has an ID
        for (PurchaseDetail detail : details) {
            detail.setPurchase(saved); // Set the saved purchase with ID
            purchaseDetailRepository.save(detail);
        }

        // Step 3: Load Purchase with details for response
        Purchase finalPurchase = purchaseRepository.findById(saved.getId())
                .orElse(saved);
        finalPurchase.setPurchaseDetails(details); // Ensure details are in response

        return convertToResponseDTO(finalPurchase);
    }

    public Optional<PurchaseResponseDTO> updatePurchase(Integer id, PurchaseUpdateRequestDTO requestDTO) {
        return purchaseRepository.findById(id).map(existingPurchase -> {
            if (requestDTO.getStatus() != null) {
                existingPurchase.setStatus(requestDTO.getStatus());
            }
            
            Purchase updated = purchaseRepository.save(existingPurchase);
            return convertToResponseDTO(updated);
        });
    }

    public void deletePurchase(Integer id) {
        purchaseRepository.deleteById(id);
    }

    public boolean purchaseExists(Integer id) {
        return purchaseRepository.existsById(id);
    }

    public List<PurchaseResponseDTO> getPurchasesByUserEmail(String email) {
        List<Purchase> purchases = purchaseRepository.findByUserEmail(email);
        return purchases.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PurchaseResponseDTO> getPurchasesByUserEmailAndStatus(String email, String status) {
        List<Purchase> purchases = purchaseRepository.findByUserEmailAndStatus(email, status);
        return purchases.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private PurchaseResponseDTO convertToResponseDTO(Purchase purchase) {
        PurchaseResponseDTO dto = new PurchaseResponseDTO();
        dto.setId(purchase.getId());
        dto.setStatus(purchase.getStatus());
        dto.setDate(purchase.getDate());
        dto.setTotal_cost(purchase.getTotal_cost());
        dto.setTotal_items(purchase.getTotal_items());
        
        if (purchase.getPurchaseDetails() != null) {
            dto.setPurchaseDetails(purchase.getPurchaseDetails().stream()
                    .map(this::convertDetailToResponseDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    private PurchaseDetailResponseDTO convertDetailToResponseDTO(PurchaseDetail detail) {
        PurchaseDetailResponseDTO dto = new PurchaseDetailResponseDTO();
        dto.setCost(detail.getCost());
        dto.setItems(detail.getItems());
        
        // Convert product to DTO
        ProductResponseDTO productDTO = new ProductResponseDTO();
        Product product = detail.getProduct();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setNumber_pages(product.getNumber_pages());
        productDTO.setCost(product.getCost());
        
        if (product instanceof Book) {
            Book book = (Book) product;
            productDTO.setAuthor(book.getAuthor());
            productDTO.setLanguage(book.getLanguage());
            productDTO.setTopic(book.getTopic());
        } else if (product instanceof Notebook) {
            Notebook notebook = (Notebook) product;
            productDTO.setLineType(notebook.getLineType());
        }
        
        dto.setProduct(productDTO);
        return dto;
    }
}

