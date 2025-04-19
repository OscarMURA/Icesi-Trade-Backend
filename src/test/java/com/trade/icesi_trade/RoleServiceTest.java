package com.trade.icesi_trade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.trade.icesi_trade.Service.Impl.RoleServiceImpl;
import com.trade.icesi_trade.model.Role;
import com.trade.icesi_trade.model.RolePermission;
import com.trade.icesi_trade.model.Permission;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
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
    void testSaveRole_Success() {
        Permission permission = new Permission(1L, "CREATE_USER", "Permiso para crear usuarios");
        List<Permission> permissions = List.of(permission);

        when(roleRepository.save(role)).thenReturn(role);
        when(rolePermissionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Role savedRole = roleService.saveRole(role, permissions);

        assertNotNull(savedRole);
        assertEquals(role.getName(), savedRole.getName());
        verify(roleRepository, times(1)).save(role);
        verify(rolePermissionRepository, times(1)).save(any());
    }

    @Test
    void testSaveRole_ThrowsException_WhenNameIsNull() {
        Role invalidRole = new Role(1L, null, "sin nombre");
        List<Permission> permissions = List.of(new Permission(1L, "TEST", "Test"));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            roleService.saveRole(invalidRole, permissions);
        });

        assertEquals("El nombre del rol es obligatorio.", thrown.getMessage());
    }

    @Test
    void testSaveRole_ThrowsException_WhenPermissionsIsNullOrEmpty() {
        Role validRole = new Role(1L, "USER", "Rol válido");

        IllegalArgumentException thrown1 = assertThrows(IllegalArgumentException.class, () -> {
            roleService.saveRole(validRole, null);
        });

        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> {
            roleService.saveRole(validRole, Collections.emptyList());
        });

        assertEquals("El rol debe tener al menos un permiso asignado.", thrown1.getMessage());
        assertEquals("El rol debe tener al menos un permiso asignado.", thrown2.getMessage());
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