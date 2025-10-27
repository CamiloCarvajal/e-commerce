package com.camilo.ecommerce.infraestructure.entry_points.rest;

import com.camilo.ecommerce.application.port.Crypto;
import com.camilo.ecommerce.application.usecase.UserUseCase;
import com.camilo.ecommerce.domain.model.User;
import com.camilo.ecommerce.domain.repository.UserRepository;
import com.camilo.ecommerce.infraestructure.driven_adapters.security.JwtService;
import com.camilo.ecommerce.infraestructure.entry_points.dto.LoginRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.LoginResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private Crypto crypto;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            User user = userRepository.findById(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

            if (!crypto.validatePassword(request.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid credentials");
            }

            String token = jwtService.generateToken(user);
            String role = user.getRole() != null ? user.getRole().getName() : "USER";
            
            LoginResponseDTO response = new LoginResponseDTO(token, user.getEmail(), role);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody com.camilo.ecommerce.infraestructure.entry_points.dto.UserRequestDTO request) {
        try {
            if (userRepository.existsById(request.getEmail())) {
                return ResponseEntity.badRequest().body("User already exists");
            }
            
            return ResponseEntity.ok(userUseCase.createUser(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }
}

