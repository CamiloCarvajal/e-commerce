package com.camilo.ecommerce.application.usecase;

import com.camilo.ecommerce.application.port.Crypto;
import com.camilo.ecommerce.domain.model.Role;
import com.camilo.ecommerce.domain.model.User;
import com.camilo.ecommerce.domain.repository.RoleRepository;
import com.camilo.ecommerce.domain.repository.UserRepository;
import com.camilo.ecommerce.infraestructure.entry_points.dto.UserRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.UserResponseDTO;
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
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Crypto crypto;

    @InjectMocks
    private UserUseCase userUseCase;

    private User testUser;
    private Role testRole;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        // Setup test role
        testRole = new Role();
        testRole.setId(1);
        testRole.setName("CUSTOMER");

        // Setup test user
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setName("John");
        testUser.setLast_name("Doe");
        testUser.setAddress("123 Main St");
        testUser.setCity("New York");
        testUser.setCountry("USA");
        testUser.setPhone("1234567890");
        testUser.setPassword("encrypted_password");
        testUser.setRole(testRole);

        // Setup request DTO
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@example.com");
        userRequestDTO.setName("John");
        userRequestDTO.setLast_name("Doe");
        userRequestDTO.setAddress("123 Main St");
        userRequestDTO.setCity("New York");
        userRequestDTO.setCountry("USA");
        userRequestDTO.setPhone("1234567890");
        userRequestDTO.setPassword("password123");
        userRequestDTO.setRoleId(1);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserResponseDTO> result = userUseCase.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(testUser));

        // When
        Optional<UserResponseDTO> result = userUseCase.getUserById("test@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findById("test@example.com");
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById("nonexistent@example.com")).thenReturn(Optional.empty());

        // When
        Optional<UserResponseDTO> result = userUseCase.getUserById("nonexistent@example.com");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        // Given
        when(roleRepository.findById(1)).thenReturn(Optional.of(testRole));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponseDTO result = userUseCase.createUser(userRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("John", result.getName());
        verify(userRepository, times(1)).save(any(User.class));
        verify(roleRepository, times(1)).findById(1);
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() {
        // Given
        UserRequestDTO updateDTO = new UserRequestDTO();
        updateDTO.setName("Jane");
        
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        Optional<UserResponseDTO> result = userUseCase.updateUser("test@example.com", updateDTO);

        // Then
        assertTrue(result.isPresent());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserNotExists_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById("nonexistent@example.com")).thenReturn(Optional.empty());

        // When
        Optional<UserResponseDTO> result = userUseCase.updateUser("nonexistent@example.com", userRequestDTO);

        // Then
        assertTrue(result.isEmpty());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldCallRepository() {
        // When
        userUseCase.deleteUser("test@example.com");

        // Then
        verify(userRepository, times(1)).deleteById("test@example.com");
    }

    @Test
    void userExists_WhenUserExists_ShouldReturnTrue() {
        // Given
        when(userRepository.existsById("test@example.com")).thenReturn(true);

        // When
        boolean result = userUseCase.userExists("test@example.com");

        // Then
        assertTrue(result);
        verify(userRepository, times(1)).existsById("test@example.com");
    }

    @Test
    void userExists_WhenUserNotExists_ShouldReturnFalse() {
        // Given
        when(userRepository.existsById("nonexistent@example.com")).thenReturn(false);

        // When
        boolean result = userUseCase.userExists("nonexistent@example.com");

        // Then
        assertFalse(result);
    }
}

