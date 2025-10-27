package com.camilo.ecommerce.application.usecase;

import com.camilo.ecommerce.domain.model.Role;
import com.camilo.ecommerce.domain.repository.RoleRepository;
import com.camilo.ecommerce.infraestructure.entry_points.dto.RoleRequestDTO;
import com.camilo.ecommerce.infraestructure.entry_points.dto.RoleResponseDTO;
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
class RoleUseCaseTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleUseCase roleUseCase;

    private Role testRole;
    private RoleRequestDTO roleRequestDTO;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1);
        testRole.setName("ADMIN");

        roleRequestDTO = new RoleRequestDTO();
        roleRequestDTO.setName("ADMIN");
    }

    @Test
    void getAllRoles_ShouldReturnListOfRoles() {
        
        List<Role> roles = Arrays.asList(testRole);
        when(roleRepository.findAll()).thenReturn(roles);

        
        List<RoleResponseDTO> result = roleUseCase.getAllRoles();

        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void getRoleById_WhenRoleExists_ShouldReturnRole() {
        
        when(roleRepository.findById(1)).thenReturn(Optional.of(testRole));

        
        Optional<RoleResponseDTO> result = roleUseCase.getRoleById(1);

        
        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getName());
        verify(roleRepository, times(1)).findById(1);
    }

    @Test
    void createRole_ShouldReturnCreatedRole() {
        
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        
        RoleResponseDTO result = roleUseCase.createRole(roleRequestDTO);

        
        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void updateRole_WhenRoleExists_ShouldReturnUpdatedRole() {
        
        RoleRequestDTO updateDTO = new RoleRequestDTO();
        updateDTO.setName("CUSTOMER");
        
        when(roleRepository.findById(1)).thenReturn(Optional.of(testRole));
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        
        Optional<RoleResponseDTO> result = roleUseCase.updateRole(1, updateDTO);

        
        assertTrue(result.isPresent());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void updateRole_WhenRoleNotExists_ShouldReturnEmpty() {
        
        when(roleRepository.findById(999)).thenReturn(Optional.empty());

        
        Optional<RoleResponseDTO> result = roleUseCase.updateRole(999, roleRequestDTO);

        
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteRole_ShouldCallRepository() {
        
        roleUseCase.deleteRole(1);

        
        verify(roleRepository, times(1)).deleteById(1);
    }

    @Test
    void roleExists_ShouldReturnTrueWhenExists() {
        
        when(roleRepository.existsById(1)).thenReturn(true);

        
        boolean result = roleUseCase.roleExists(1);

        
        assertTrue(result);
    }

    @Test
    void roleExists_ShouldReturnFalseWhenNotExists() {
        
        when(roleRepository.existsById(999)).thenReturn(false);

        
        boolean result = roleUseCase.roleExists(999);

        
        assertFalse(result);
    }
}

