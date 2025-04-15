package com.trade.icesi_trade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.trade.icesi_trade.model.Notification;
import com.trade.icesi_trade.model.User;
import com.trade.icesi_trade.repository.NotificationRepository;
import com.trade.icesi_trade.Service.Impl.NotificationServiceImpl;
import com.trade.icesi_trade.Service.Interface.NotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    
    @InjectMocks
    private NotificationServiceImpl notificationService;
    
    // Variables para los distintos escenarios de notificaciones
    private Notification defaultNotification;
    private Notification secondNotification;
    private List<Notification> notificationList;
    
    // Primer setUp: Inicializa la notificación por defecto (pendiente, no leída)
    @BeforeEach
    public void setUp() {
        defaultNotification = Notification.builder()
                .id(1L)
                .read(false)
                .message("Mensaje recibido")
                .user(User.builder().id(10L).build())
                .build();
        
        secondNotification = Notification.builder()
                .id(2L)
                .read(true)
                .message("Oferta aceptada")
                .user(User.builder().id(10L).build())
                .build();
        
        notificationList = Arrays.asList(defaultNotification, secondNotification);
    }

    
    // Segundo setUp: Inicializa otra notificación (ya leída)
    @BeforeEach
    public void setUpSecondNotification() {
        secondNotification = Notification.builder()
                .id(2L)
                .read(true)
                .message("Oferta aceptada")
                .user(User.builder().id(10L).build())
                .build();
    }
    
    // Tercer setUp: Inicializa una lista con ambas notificaciones para simular el historial de un usuario
    @BeforeEach
    public void setUpNotificationList() {
        notificationList = Arrays.asList(defaultNotification, secondNotification);
    }
    
    @Test
    public void testCreateNotification_Success() {
        // Arrange: Simulamos que al guardar la notificación se retorne la misma instancia.
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act: Se crea una notificación con defaultNotification
        Notification created = notificationService.createNotification(defaultNotification);
        
        // Assert
        assertNotNull(created);
        assertEquals("Mensaje recibido", created.getMessage());
        // Como en este caso se inicializa read en false en el setUp, se espera ese valor.
        assertFalse(created.getRead());
        verify(notificationRepository, times(1)).save(defaultNotification);
    }
    
    @Test
    public void testCreateNotification_NullNotification() {
        // Act & Assert: Se espera que al intentar crear una notificación nula se lance IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationService.createNotification(null);
        });
        assertEquals("La notificación no puede ser nula.", exception.getMessage());
    }
    
    @Test
    public void testMarkNotificationAsRead_Success() {
        // Arrange: Se simula que se encuentra la notificación por ID
        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(defaultNotification));
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act: Se marca la notificación como leída
        Notification updated = notificationService.markNotificationAsRead(1L);
        
        // Assert
        assertNotNull(updated);
        assertTrue(updated.getRead());
        verify(notificationRepository, times(1)).findById(1L);
        verify(notificationRepository, times(1)).save(defaultNotification);
    }
    
    @Test
    public void testMarkNotificationAsRead_NotFound() {
        // Arrange: Simula que la notificación no se encuentra
        when(notificationRepository.findById(1L))
                .thenReturn(Optional.empty());
        
        // Act & Assert: Se espera NoSuchElementException con el mensaje adecuado.
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            notificationService.markNotificationAsRead(1L);
        });
        assertTrue(exception.getMessage().contains("Notificación no encontrada con el ID: 1"));
    }
    
    @Test
    public void testGetNotificationsByUser_Success() {
        // Arrange: Simula que al llamar a findAll() se retorna la lista completa de notificaciones.
        when(notificationRepository.findAll()).thenReturn(notificationList);
        
        // Act: Se obtienen las notificaciones del usuario con ID 10
        List<Notification> notifications = notificationService.getNotificationsByUser(10L);
        
        // Assert: Se espera que ambas notificaciones correspondan al usuario con ID 10.
        assertNotNull(notifications);
        assertEquals(2, notifications.size());
        List<Long> userIds = notifications.stream().map(n -> n.getUser().getId()).collect(Collectors.toList());
        assertTrue(userIds.stream().allMatch(id -> id.equals(10L)));
        verify(notificationRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetNotificationsByUser_NullUserId() {
        // Act & Assert: Se espera que pasando un userId nulo se lance IllegalArgumentException.
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationService.getNotificationsByUser(null);
        });
        assertEquals("El ID del usuario no puede ser nulo.", exception.getMessage());
    }
    
    @Test
    public void testGetPendingNotificationsByUser_Success() {
        // Arrange: Utilizamos la misma lista; en ella defaultNotification tiene read false.
        when(notificationRepository.findAll()).thenReturn(notificationList);
        
        // Act: Se obtienen las notificaciones pendientes del usuario con ID 10
        List<Notification> pending = notificationService.getPendingNotificationsByUser(10L);
        
        // Assert: Solo se espera que defaultNotification esté pendiente (read false)
        assertNotNull(pending);
        assertEquals(1, pending.size());
        assertFalse(pending.get(0).getRead());
        verify(notificationRepository, times(1)).findAll();
    }
}
