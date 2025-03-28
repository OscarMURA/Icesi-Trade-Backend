package com.trade.icesi_trade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.trade.icesi_trade.Service.Impl.UserServiceImpl;
import com.trade.icesi_trade.model.User;
import com.trade.icesi_trade.model.UserRole; // Import UserRole class
import com.trade.icesi_trade.repository.UserRepository;
import com.trade.icesi_trade.repository.UserRoleRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testFindUserByEmail_Success() {
        
        String email = "john.doe@example.com";
        User user = new User(1L, "john.doe@example.com", "password123", "John Doe", "123456789", LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList());
        when(userRepository.findByEmail(email)).thenReturn(user);

        
        User foundUser = userService.findUserByEmail(email);

        
        assertNotNull(foundUser);
        assertEquals("john.doe@example.com", foundUser.getEmail());
    }

    @Test
    void testSaveUser_Success() {
        
        UserRole userRole = new UserRole(); // Crear un UserRole simulado
        User user = new User(1L, "john.doe@example.com", "password123", "John Doe", "123456789", LocalDateTime.now(), LocalDateTime.now(), Collections.singletonList(userRole));
        when(userRoleRepository.countByUser_Id(user.getId())).thenReturn(1L);

        when(userRepository.save(user)).thenReturn(user);

        
        User savedUser = userService.saveUser(user);

        
        assertNotNull(savedUser);
        assertEquals("john.doe@example.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(user); // Verifica que el método save haya sido llamado una vez
    }

    @Test
    void testSaveUser_ThrowsException_WhenNoEmail() {
        
        User user = new User(1L, null, "password123", "John Doe", "123456789", LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList());
        when(userRoleRepository.countByUser_Id(user.getId())).thenReturn(1L);

        
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user);
        });

        assertEquals("El usuario debe tener al menos un ID y un email.", thrown.getMessage());
    }

    @Test
    void testSaveUser_ThrowsException_WhenNoRole() {
        
        User user = new User(1L, "john.doe@example.com", "password123", "John Doe", "123456789", LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList());
        when(userRoleRepository.countByUser_Id(user.getId())).thenReturn(0L); // No tiene roles asignados

        
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user);
        });

        assertEquals("El usuario debe tener al menos un rol asignado.", thrown.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        
        userService.deleteUser(userId);

        
        verify(userRepository, times(1)).deleteById(userId); // Verifica que el método deleteById haya sido llamado una vez
    }

    @Test
    void testDeleteUser_ThrowsException_WhenUserNotFound() {
        
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(userId);
        });

        assertEquals("El usuario no existe.", thrown.getMessage());
    }
}