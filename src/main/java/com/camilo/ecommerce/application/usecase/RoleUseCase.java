package com.camilo.ecommerce.application.usecase;

import com.camilo.ecommerce.domain.model.Role;
import com.camilo.ecommerce.domain.repository.RoleRepository;
import com.camilo.ecommerce.infraestructure.entry_points.dto.RoleRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.RoleResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleUseCase {

    @Autowired
    private RoleRepository roleRepository;

    public List<RoleResponseDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<RoleResponseDTO> getRoleById(Integer id) {
        return roleRepository.findById(id).map(this::convertToResponseDTO);
    }

    public RoleResponseDTO createRole(RoleRequestDTO requestDTO) {
        Role role = new Role();
        role.setName(requestDTO.getName());
        Role saved = roleRepository.save(role);
        return convertToResponseDTO(saved);
    }

    public Optional<RoleResponseDTO> updateRole(Integer id, RoleRequestDTO requestDTO) {
        return roleRepository.findById(id).map(existingRole -> {
            existingRole.setName(requestDTO.getName());
            Role updated = roleRepository.save(existingRole);
            return convertToResponseDTO(updated);
        });
    }

    public void deleteRole(Integer id) {
        roleRepository.deleteById(id);
    }

    public boolean roleExists(Integer id) {
        return roleRepository.existsById(id);
    }

    private RoleResponseDTO convertToResponseDTO(Role role) {
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }
}

