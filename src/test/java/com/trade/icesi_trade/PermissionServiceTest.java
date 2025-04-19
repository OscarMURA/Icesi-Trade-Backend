package com.trade.icesi_trade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.trade.icesi_trade.Service.Impl.PermissionServiceImpl;
import com.trade.icesi_trade.model.Permission;
import com.trade.icesi_trade.repository.PermissionRepository;

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
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    private Permission permission;

    @BeforeEach
    void setUp() {
        permission = new Permission(1L, "CREATE_USER", "Allows creating users");
    }

    @Test
    void testFindPermissionByName_Success() {
        when(permissionRepository.findByName("CREATE_USER")).thenReturn(permission);

        Permission foundPermission = permissionService.findPermissionByName("CREATE_USER");

        assertNotNull(foundPermission);
        assertEquals("CREATE_USER", foundPermission.getName());
    }

    @Test
    void testFindPermissionByName_ThrowsException_WhenNotFound() {
        when(permissionRepository.findByName("INVALID")).thenReturn(null);

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            permissionService.findPermissionByName("INVALID");
        });

        assertEquals("Permiso no encontrado.", thrown.getMessage());
    }

    @Test
    void testSavePermission_Success() {
        when(permissionRepository.save(permission)).thenReturn(permission);

        Permission savedPermission = permissionService.savePermission(permission);

        assertNotNull(savedPermission);
        assertEquals("CREATE_USER", savedPermission.getName());
        verify(permissionRepository, times(1)).save(permission);
    }

    @Test
    void testSavePermission_ThrowsException_WhenNameIsNull() {
        Permission invalidPermission = new Permission(2L, null, "No name");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            permissionService.savePermission(invalidPermission);
        });

        assertEquals("El permiso debe tener un nombre.", thrown.getMessage());
    }

    @Test
    void testSavePermission_ThrowsException_WhenNameIsEmpty() {
        Permission invalidPermission = new Permission(2L, "", "No name");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            permissionService.savePermission(invalidPermission);
        });

        assertEquals("El permiso debe tener un nombre.", thrown.getMessage());
    }

    @Test
    void testDeletePermission_Success() {
        when(permissionRepository.existsById(permission.getId())).thenReturn(true);

        permissionService.deletePermission(permission.getId());

        verify(permissionRepository, times(1)).deleteById(permission.getId());
    }

    @Test
    void testDeletePermission_ThrowsException_WhenPermissionNotFound() {
        when(permissionRepository.existsById(permission.getId())).thenReturn(false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            permissionService.deletePermission(permission.getId());
        });

        assertEquals("El permiso no existe.", thrown.getMessage());
    }

    @Test
    void testUpdatePermission_Success() {
        Permission updated = new Permission(1L, "UPDATE_USER", "Allows updating users");

        when(permissionRepository.findById(permission.getId())).thenReturn(Optional.of(permission));
        when(permissionRepository.save(any(Permission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Permission result = permissionService.updatePermission(updated, permission.getId());

        assertNotNull(result);
        assertEquals("UPDATE_USER", result.getName());
        assertEquals("Allows updating users", result.getDescription());
        verify(permissionRepository, times(1)).save(any(Permission.class));
    }

    @Test
    void testUpdatePermission_ThrowsException_WhenNameIsNull() {
        Permission invalid = new Permission(1L, null, "Desc");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            permissionService.updatePermission(invalid, permission.getId());
        });

        assertEquals("El permiso debe tener un nombre.", thrown.getMessage());
    }

    @Test
    void testUpdatePermission_ThrowsException_WhenNameIsEmpty() {
        Permission invalid = new Permission(1L, "", "Desc");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            permissionService.updatePermission(invalid, permission.getId());
        });

        assertEquals("El permiso debe tener un nombre.", thrown.getMessage());
    }

    @Test
    void testUpdatePermission_ThrowsException_WhenPermissionNotFound() {
        Permission updated = new Permission(1L, "ANY", "desc");

        when(permissionRepository.findById(permission.getId())).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            permissionService.updatePermission(updated, permission.getId());
        });

        assertEquals("Permiso no encontrado.", thrown.getMessage());
    }
}