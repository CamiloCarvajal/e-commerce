package com.camilo.ecommerce.application.usecase;

import com.camilo.ecommerce.application.port.Crypto;
import com.camilo.ecommerce.domain.model.Role;
import com.camilo.ecommerce.domain.model.User;
import com.camilo.ecommerce.domain.repository.RoleRepository;
import com.camilo.ecommerce.domain.repository.UserRepository;
import com.camilo.ecommerce.infraestructure.entry_points.dto.UserRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserUseCase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Crypto crypto;

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> getUserById(String email) {
        return userRepository.findById(email).map(this::convertToResponseDTO);
    }

    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        User user = convertFromRequestDTO(requestDTO);
        User saved = userRepository.save(user);
        return convertToResponseDTO(saved);
    }

    public Optional<UserResponseDTO> updateUser(String email, UserRequestDTO requestDTO) {
        return userRepository.findById(email).map(existingUser -> {
            updateUserFromDTO(existingUser, requestDTO);
            User updated = userRepository.save(existingUser);
            return convertToResponseDTO(updated);
        });
    }

    public void deleteUser(String email) {
        userRepository.deleteById(email);
    }

    public boolean userExists(String email) {
        return userRepository.existsById(email);
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setLast_name(user.getLast_name());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setCountry(user.getCountry());
        dto.setPhone(user.getPhone());
        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getId());
        }
        return dto;
    }

    private User convertFromRequestDTO(UserRequestDTO requestDTO) {
        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setName(requestDTO.getName());
        user.setLast_name(requestDTO.getLast_name());
        user.setAddress(requestDTO.getAddress());
        user.setCity(requestDTO.getCity());
        user.setCountry(requestDTO.getCountry());
        user.setPhone(requestDTO.getPhone());
        user.setPassword(requestDTO.getPassword());
        
        if (requestDTO.getRoleId() != null) {
            roleRepository.findById(requestDTO.getRoleId()).ifPresent(user::setRole);
        }
        
        return user;
    }

    private void updateUserFromDTO(User user, UserRequestDTO requestDTO) {
        if (requestDTO.getName() != null) {
            user.setName(requestDTO.getName());
        }
        if (requestDTO.getLast_name() != null) {
            user.setLast_name(requestDTO.getLast_name());
        }
        if (requestDTO.getAddress() != null) {
            user.setAddress(requestDTO.getAddress());
        }
        if (requestDTO.getCity() != null) {
            user.setCity(requestDTO.getCity());
        }
        if (requestDTO.getCountry() != null) {
            user.setCountry(requestDTO.getCountry());
        }
        if (requestDTO.getPhone() != null) {
            user.setPhone(requestDTO.getPhone());
        }
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            user.setPassword(requestDTO.getPassword());
        }
        if (requestDTO.getRoleId() != null) {
            roleRepository.findById(requestDTO.getRoleId()).ifPresent(user::setRole);
        }
    }
}
