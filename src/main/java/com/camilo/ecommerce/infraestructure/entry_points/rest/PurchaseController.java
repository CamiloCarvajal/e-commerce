package com.camilo.ecommerce.infraestructure.entry_points.rest;

import com.camilo.ecommerce.application.usecase.PurchaseUseCase;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PurchaseRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PurchaseResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseUseCase purchaseUseCase;

    @GetMapping
    public ResponseEntity<List<PurchaseResponseDTO>> getAllPurchases() {
        List<PurchaseResponseDTO> purchases = purchaseUseCase.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponseDTO> getPurchaseById(@PathVariable Integer id) {
        Optional<PurchaseResponseDTO> purchase = purchaseUseCase.getPurchaseById(id);
        return purchase.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PurchaseResponseDTO> createPurchase(@Valid @RequestBody PurchaseRequestDTO requestDTO) {
        try {
            PurchaseResponseDTO created = purchaseUseCase.createPurchase(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseResponseDTO> updatePurchase(
            @PathVariable Integer id,
            @Valid @RequestBody PurchaseRequestDTO requestDTO) {
        Optional<PurchaseResponseDTO> updated = purchaseUseCase.updatePurchase(id, requestDTO);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Integer id) {
        if (purchaseUseCase.purchaseExists(id)) {
            purchaseUseCase.deletePurchase(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<PurchaseResponseDTO>> getPurchasesByUser(@PathVariable String email) {
        List<PurchaseResponseDTO> purchases = purchaseUseCase.getPurchasesByUserEmail(email);
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/user/{email}/status/{status}")
    public ResponseEntity<List<PurchaseResponseDTO>> getPurchasesByUserAndStatus(
            @PathVariable String email,
            @PathVariable String status) {
        List<PurchaseResponseDTO> purchases = purchaseUseCase.getPurchasesByUserEmailAndStatus(email, status);
        return ResponseEntity.ok(purchases);
    }
}

