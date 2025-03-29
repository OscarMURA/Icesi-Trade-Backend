package com.trade.icesi_trade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.trade.icesi_trade.Service.Impl.RoleServiceImpl;
import com.trade.icesi_trade.model.Role;
import com.trade.icesi_trade.model.RolePermission;
import com.trade.icesi_trade.repository.RolePermissionRepository;
import com.trade.icesi_trade.repository.RoleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(1L, "ADMIN", "Administrator role");
    }

    @Test
    void testFindRoleByName_Success() {
        when(roleRepository.findByName("ADMIN")).thenReturn(role);
        Role foundRole = roleService.findRoleByName("ADMIN");
        assertNotNull(foundRole);
        assertEquals("ADMIN", foundRole.getName());
    }

    @Test
    void testFindRoleByName_ThrowsException_WhenNameIsNull() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            roleService.findRoleByName(null);
        });
        assertEquals("El nombre del rol no puede ser nulo.", thrown.getMessage());
    }

    @Test
    void testSaveRole_Success() {
        when(rolePermissionRepository.findByRole_Id(role.getId())).thenReturn(Collections.singletonList(new RolePermission()));
        when(roleRepository.save(role)).thenReturn(role);

        Role savedRole = roleService.saveRole(role);

        assertNotNull(savedRole);
        assertEquals(role.getName(), savedRole.getName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testSaveRole_ThrowsException_WhenRoleHasNoId() {
        Role invalidRole = new Role(null, "NO_ID", "No id");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            roleService.saveRole(invalidRole);
        });

        assertEquals("El rol debe tener al menos un ID y un nombre.", thrown.getMessage());
    }

    @Test
    void testSaveRole_ThrowsException_WhenRoleHasNoIdOrName() {
        Role invalidRole = new Role(3L, null, "No name");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            roleService.saveRole(invalidRole);
        });

        assertEquals("El rol debe tener al menos un ID y un nombre.", thrown.getMessage());
    }

    @Test
    void testSaveRole_ThrowsException_WhenNoPermissions() {
        when(rolePermissionRepository.findByRole_Id(role.getId())).thenReturn(Collections.emptyList());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            roleService.saveRole(role);
        });

        assertEquals("El rol debe tener al menos un permiso asignado.", thrown.getMessage());
    }

    @Test
    void testDeleteRole_Success() {
        when(roleRepository.existsById(role.getId())).thenReturn(true);

        roleService.deleteRole(role.getId());

        verify(roleRepository, times(1)).deleteById(role.getId());
    }

    @Test
    void testDeleteRole_ThrowsException_WhenRoleNotFound() {
        when(roleRepository.existsById(role.getId())).thenReturn(false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            roleService.deleteRole(role.getId());
        });

        assertEquals("El rol no existe.", thrown.getMessage());
    }

    @Test
    void testUpdateRole_Success() {
        when(roleRepository.existsById(role.getId())).thenReturn(true);
        when(rolePermissionRepository.findByRole_Id(role.getId())).thenReturn(Collections.singletonList(new RolePermission()));
        when(roleRepository.save(role)).thenReturn(role);

        Role updatedRole = roleService.updateRole(role.getId(), role);

        assertNotNull(updatedRole);
        assertEquals(role.getName(), updatedRole.getName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testUpdateRole_ThrowsException_WhenRoleIsNull() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            roleService.updateRole(null, role);
        });

        assertEquals("El ID del rol y el rol actualizado no pueden ser nulos.", thrown.getMessage());
    }

    @Test
    void testUpdateRole_ThrowsException_WhenIdIsNull() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            roleService.updateRole(5L, null);
        });

        assertEquals("El ID del rol y el rol actualizado no pueden ser nulos.", thrown.getMessage());
    }

    @Test
    void testUpdateRole_ThrowsException_WhenRoleNotFound() {
        when(roleRepository.existsById(role.getId())).thenReturn(false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            roleService.updateRole(role.getId(), role);
        });

        assertEquals("El rol no existe.", thrown.getMessage());
    }

    @Test
    void testUpdateRole_ThrowsException_WhenNoPermissions() {
        when(roleRepository.existsById(role.getId())).thenReturn(true);
        when(rolePermissionRepository.findByRole_Id(role.getId())).thenReturn(Collections.emptyList());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            roleService.updateRole(role.getId(), role);
        });

        assertEquals("El rol debe tener al menos un permiso asignado.", thrown.getMessage());
    }

    @Test
    void testFindAllRoles_Success() {
        when(roleRepository.findAll()).thenReturn(Collections.singletonList(role));

        List<Role> roles = roleService.findAllRoles();

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals(role.getName(), roles.get(0).getName());
    }

    @Test
    void testFindAllById_Success() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(roleRepository.findAllById(ids)).thenReturn(Collections.singletonList(role));

        List<Role> roles = roleService.findAllById(ids);

        assertNotNull(roles);
        assertEquals(1, roles.size());
    }

    @Test
    void testFindAllById_ThrowsException_WhenIdsAreNullOrEmpty() {
        IllegalArgumentException thrown1 = assertThrows(IllegalArgumentException.class, () -> {
            roleService.findAllById(null);
        });

        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> {
            roleService.findAllById(Collections.emptyList());
        });

        assertEquals("La lista de IDs no puede ser nula o vacía.", thrown1.getMessage());
        assertEquals("La lista de IDs no puede ser nula o vacía.", thrown2.getMessage());
    }

    @Test
    void testFindById_Success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Optional<Role> foundRole = roleService.findById(1L);

        assertTrue(foundRole.isPresent());
        assertEquals(role.getName(), foundRole.get().getName());
    }

    @Test
    void testFindById_ReturnsEmpty_WhenNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Role> foundRole = roleService.findById(1L);

        assertFalse(foundRole.isPresent());
    }
}