package com.camilo.ecommerce.infraestructure.entry_points.rest;

import com.camilo.ecommerce.application.usecase.PurchaseUseCase;
import com.camilo.ecommerce.application.usecase.UserUseCase;
import com.camilo.ecommerce.infraestructure.driven_adapters.security.SecurityUtils;
import com.camilo.ecommerce.infraestructure.entry_points.dto.PurchaseResponseDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.UserRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.UserResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserUseCase userUseCase;

    @MockBean
    private PurchaseUseCase purchaseUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDTO testUserResponse;
    private UserRequestDTO testUserRequest;

    @BeforeEach
    void setUp() {
        // Setup test user response
        testUserResponse = new UserResponseDTO();
        testUserResponse.setEmail("test@example.com");
        testUserResponse.setName("John");
        testUserResponse.setLast_name("Doe");
        testUserResponse.setAddress("123 Main St");
        testUserResponse.setCity("New York");
        testUserResponse.setCountry("USA");
        testUserResponse.setPhone("1234567890");
        testUserResponse.setRoleId(1);

        // Setup test user request
        testUserRequest = new UserRequestDTO();
        testUserRequest.setEmail("test@example.com");
        testUserRequest.setName("John");
        testUserRequest.setLast_name("Doe");
        testUserRequest.setAddress("123 Main St");
        testUserRequest.setCity("New York");
        testUserRequest.setCountry("USA");
        testUserRequest.setPhone("1234567890");
        testUserRequest.setPassword("password123");
        testUserRequest.setRoleId(1);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        
        List<UserResponseDTO> users = Arrays.asList(testUserResponse);
        when(userUseCase.getAllUsers()).thenReturn(users);

        
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    void getUserById_WhenEmailProvided_ShouldReturnUser() throws Exception {
        
        when(userUseCase.getUserById("test@example.com")).thenReturn(Optional.of(testUserResponse));

        
        mockMvc.perform(get("/api/users/test@example.com")
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void getUserById_WhenEmailNotProvided_ShouldUseJWTToken() throws Exception {
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn("test@example.com");
            when(userUseCase.getUserById("test@example.com")).thenReturn(Optional.of(testUserResponse));

            
            mockMvc.perform(get("/api/users")
                            .header("Authorization", "Bearer mock-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("test@example.com"));
        }
    }

    @Test
    void getUserById_WhenUnauthorized_ShouldReturn401() throws Exception {
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(null);

            
            mockMvc.perform(get("/api/users")
                            .header("Authorization", "Bearer mock-token"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void getUserById_WhenUserNotFound_ShouldReturn404() throws Exception {
        
        when(userUseCase.getUserById("nonexistent@example.com")).thenReturn(Optional.empty());

        
        mockMvc.perform(get("/api/users/nonexistent@example.com")
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        
        when(userUseCase.createUser(any(UserRequestDTO.class))).thenReturn(testUserResponse);

        
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserRequest))
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void createUser_WhenBadRequest_ShouldReturn400() throws Exception {
        
        when(userUseCase.createUser(any(UserRequestDTO.class))).thenThrow(new RuntimeException("Error"));

        
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserRequest))
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        
        when(userUseCase.updateUser("test@example.com", any(UserRequestDTO.class)))
                .thenReturn(Optional.of(testUserResponse));

        
        mockMvc.perform(put("/api/users/test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserRequest))
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void updateUser_WhenUserNotFound_ShouldReturn404() throws Exception {
        
        when(userUseCase.updateUser("nonexistent@example.com", any(UserRequestDTO.class)))
                .thenReturn(Optional.empty());

        
        mockMvc.perform(put("/api/users/nonexistent@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserRequest))
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturn204() throws Exception {
        
        when(userUseCase.userExists("test@example.com")).thenReturn(true);
        doNothing().when(userUseCase).deleteUser("test@example.com");

        
        mockMvc.perform(delete("/api/users/test@example.com")
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldReturn404() throws Exception {
        
        when(userUseCase.userExists("nonexistent@example.com")).thenReturn(false);

        
        mockMvc.perform(delete("/api/users/nonexistent@example.com")
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserInfoWithPurchases_ShouldReturnUserWithPurchases() throws Exception {
        
        List<PurchaseResponseDTO> purchases = Arrays.asList(new PurchaseResponseDTO());
        testUserResponse.setPurchases(purchases);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn("test@example.com");
            when(userUseCase.getUserById("test@example.com")).thenReturn(Optional.of(testUserResponse));
            when(purchaseUseCase.getPurchasesByUserEmail("test@example.com")).thenReturn(purchases);

            
            mockMvc.perform(get("/api/users/test@example.com/info")
                            .header("Authorization", "Bearer mock-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("test@example.com"))
                    .andExpect(jsonPath("$.purchases").exists());
        }
    }

    @Test
    void getUserInfoWithPurchases_WhenUnauthorized_ShouldReturn401() throws Exception {
        
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserEmail).thenReturn(null);

            
            mockMvc.perform(get("/api/users/test@example.com/info")
                            .header("Authorization", "Bearer mock-token"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void createUser_WithInvalidData_ShouldReturn400() throws Exception {
        
        UserRequestDTO invalidRequest = new UserRequestDTO();
        invalidRequest.setEmail("invalid-email"); // Invalid email

        
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isBadRequest());
    }
}

