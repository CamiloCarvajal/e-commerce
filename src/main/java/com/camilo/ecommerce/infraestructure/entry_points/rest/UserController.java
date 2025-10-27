package com.camilo.ecommerce.infraestructure.entry_points.rest;

import com.camilo.ecommerce.application.usecase.PurchaseUseCase;
import com.camilo.ecommerce.application.usecase.UserUseCase;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PurchaseResponseDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.UserRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private PurchaseUseCase purchaseUseCase;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userUseCase.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String email) {
        Optional<UserResponseDTO> user = userUseCase.getUserById(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        try {
            UserResponseDTO created = userUseCase.createUser(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable String email,
            @Valid @RequestBody UserRequestDTO requestDTO) {
        Optional<UserResponseDTO> updated = userUseCase.updateUser(email, requestDTO);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        if (userUseCase.userExists(email)) {
            userUseCase.deleteUser(email);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{email}/info")
    public ResponseEntity<UserResponseDTO> getUserInfoWithPurchases(@PathVariable String email) {
        Optional<UserResponseDTO> user = userUseCase.getUserById(email);
        if (user.isPresent()) {
            UserResponseDTO userDTO = user.get();
            List<PurchaseResponseDTO> purchases = purchaseUseCase.getPurchasesByUserEmail(email);
            userDTO.setPurchases(purchases);
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.notFound().build();
    }
}

