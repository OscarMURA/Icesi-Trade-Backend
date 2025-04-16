// package com.trade.icesi_trade;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyList;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;
// import java.util.NoSuchElementException;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.mockito.junit.jupiter.MockitoSettings;
// import org.mockito.quality.Strictness;

// import com.trade.icesi_trade.Service.Impl.RoleServiceImpl;
// import com.trade.icesi_trade.Service.Impl.UserServiceImpl;
// import com.trade.icesi_trade.model.Role;
// import com.trade.icesi_trade.model.User;
// import com.trade.icesi_trade.model.UserRole;
// import com.trade.icesi_trade.repository.UserRepository;
// import com.trade.icesi_trade.repository.UserRoleRepository;

// @ExtendWith(MockitoExtension.class)
// @MockitoSettings(strictness = Strictness.LENIENT)
// public class UserServiceTest {
//     @Mock
//     private UserRepository userRepository;

//     @Mock
//     private UserRoleRepository userRoleRepository;

//     @Mock
//     private RoleServiceImpl roleService;

//     @InjectMocks
//     private UserServiceImpl userService;

//     private UserRole userRole;
//     private User user;

//     @BeforeEach
//     void setUp() {
//         userRole = new UserRole();
//         user = new User(1L, "john.doe@example.com", "password123", "John Doe", "123456789", 
//                             LocalDateTime.now(), LocalDateTime.now(), Collections.singletonList(userRole));
//     }

//     @Test
//     void testFindUserByEmail_Success() {
//         String email = "john.doe@example.com";
//         when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

//         User foundUser = userService.findUserByEmail(email);

//         assertNotNull(foundUser);
//         assertEquals("john.doe@example.com", foundUser.getEmail());
//     }

//     @Test
//     void testFindUserByEmail_ThrowsException_WhenUserNotFound(){
//         String email = "john.doe@example.com";
//         when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

//         NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
//             userService.findUserByEmail(email);
//         });

//         assertEquals("Usuario no encontrado.", thrown.getMessage());
//     }

//     @Test
//     void testFindUserById_Succes(){
//         Long id = 1L;

//         when(userRepository.findById(id)).thenReturn(Optional.of(user));
        
//         User foundUser = userService.findUserById(id);

//         assertNotNull(foundUser);
//         assertEquals(1L, foundUser.getId());
//     }

//     @Test
//     void testFindUserById_ThrowsException_WhenUserNotFound() {
//         Long id = 1L;
//         when(userRepository.findById(id)).thenReturn(Optional.empty());
        
//         NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
//             userService.findUserById(id);
//         });

//         assertEquals("Usuario no encontrado.", thrown.getMessage());
//     }

//     void testFindAllUsers_Success() {
//         when(userRepository.findAll()).thenReturn(List.of(user));
//         List<User> users = userService.findAllUsers();
        
//         assertNotNull(users);
//         assertEquals(1, users.size());
//         assertEquals(user.getEmail(), users.get(0).getEmail());
//     }

//     @Test
//     void testFindAllUsers_ReturnsEmptyList_WhenNoUsersExist() {
//         when(userRepository.findAll()).thenReturn(Collections.emptyList());

//         List<User> users = userService.findAllUsers();

//         assertNotNull(users);
//         assertEquals(0, users.size());
//     }

//     @Test
//     void testSaveUser_Success() {        
//         when(userRoleRepository.countByUser_Id(user.getId())).thenReturn(1L);

//         when(userRepository.save(user)).thenReturn(user);
        
//         User savedUser = userService.saveUser(user);
        
//         assertNotNull(savedUser);
//         assertEquals(user.getEmail(), savedUser.getEmail());
//         verify(userRepository, times(1)).save(user);
//     }

//     @Test
//     void testSaveUser_ThrowsException_WhenNoEmail() {
//         User userWithoutEmail = new User(1L, null, "password123", "John Doe", "123456789", 
//                                          LocalDateTime.now(), LocalDateTime.now(), Collections.singletonList(userRole));
        
//         when(userRoleRepository.countByUser_Id(userWithoutEmail.getId())).thenReturn(1L);
        
//         IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
//             userService.saveUser(userWithoutEmail);
//         });

//         assertEquals("El usuario debe tener al menos un ID y un email.", thrown.getMessage());
//     }

//     @Test
//     void testSaveUser_ThrowsException_WhenNoRole() {
//         when(userRoleRepository.countByUser_Id(user.getId())).thenReturn(0L); // No tiene roles asignados
        
//         IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
//             userService.saveUser(user);
//         });

//         assertEquals("El usuario debe tener al menos un rol asignado.", thrown.getMessage());
//     }

//     @Test
//     void testSaveUser_ThrowsException_WhenUserHasNoIdOrEmail() {
//         User invalidUser = new User(null, null, "password123", "John Doe", "123456789", 
//                                     LocalDateTime.now(), LocalDateTime.now(), Collections.singletonList(userRole));
        
//         IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
//             userService.saveUser(invalidUser);
//         });

//         assertEquals("El usuario debe tener al menos un ID y un email.", thrown.getMessage());
//     }

//     @Test
//     void testUpdateUser_ThrowsException_WhenNoEmail() {
//         User userWithoutEmail = new User(1L, null, "password123", "John Doe", "123456789", 
//                                          LocalDateTime.now(), LocalDateTime.now(), Collections.singletonList(userRole));
        
//         when(userRoleRepository.countByUser_Id(userWithoutEmail.getId())).thenReturn(1L);
        
//         IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
//             userService.updateUser(userWithoutEmail, 1L);
//         });

//         assertEquals("El usuario debe tener al menos un ID y un email.", thrown.getMessage());
//     }

//     @Test
//     void testUpdateUser_Success() {
//         User updatedUser = new User(1L, "updated@example.com", "newpassword123", "Updated User", "987654321", 
//                                     LocalDateTime.now(), LocalDateTime.now(), Collections.singletonList(userRole));
        
//         when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//         when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

//         User result = userService.updateUser(updatedUser, user.getId());

//         assertNotNull(result);
//         assertEquals("updated@example.com", result.getEmail());
//         assertEquals("Updated User", result.getName());
//         assertEquals("987654321", result.getPhone());
//         assertEquals("newpassword123", result.getPassword());
//         verify(userRepository, times(1)).findById(user.getId());
//         verify(userRepository, times(1)).save(any(User.class));
//     }

//     @Test
//     void testUpdateUser_ThrowsException_WhenUserHasNoIdOrEmail() {
//         User invalidUser = new User(null, null, "password123", "John Doe", "123456789", 
//                                     LocalDateTime.now(), LocalDateTime.now(), Collections.singletonList(userRole));
        
//         IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
//             userService.updateUser(invalidUser, 1L);
//         });

//         assertEquals("El usuario debe tener al menos un ID y un email.", thrown.getMessage());
//     }

//     @Test
//     void testUpdateUser_ThrowsException_WhenUserNotFound() {
//         Long userId = 2L;
//         User updatedUser = new User(userId, "john.updated@example.com", "newpassword123", "John Updated", "987654321", 
//                                     LocalDateTime.now(), LocalDateTime.now(), Collections.singletonList(userRole));
        
//         when(userRepository.findById(userId)).thenReturn(Optional.empty());

//         NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
//             userService.updateUser(updatedUser, userId);
//         });

//         assertEquals("Usuario no encontrado.", thrown.getMessage());
//     }

//     @Test
//     void testUpdateUserRoles_Success() {
//         Long userId = 1L;
//         List<Long> newRoleIds = Arrays.asList(2L, 3L);

//         User user = new User();
//         user.setId(userId);

//         UserRole existingRole = new UserRole();
//         existingRole.setId(1L);

//         Role role1 = new Role();
//         role1.setId(2L);

//         Role role2 = new Role();
//         role2.setId(3L);

//         when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//         when(userRoleRepository.findByUser_Id(userId)).thenReturn(Collections.singletonList(existingRole));
//         when(roleService.findAllById(newRoleIds)).thenReturn(Arrays.asList(role1, role2));

//         userService.updateUserRoles(userId, newRoleIds);

//         verify(userRoleRepository).deleteAll(Collections.singletonList(existingRole));
//         verify(userRoleRepository, times(2)).save(any(UserRole.class));
//     }

//     @Test
//     void testUpdateUserRoles_UserNotFound() {
//         Long userId = 1L;
//         List<Long> newRoleIds = Arrays.asList(2L, 3L);

//         when(userRepository.findById(userId)).thenReturn(Optional.empty());

//         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//             userService.updateUserRoles(userId, newRoleIds);
//         });

//         assertEquals("User not found", exception.getMessage());
//         verify(userRoleRepository, never()).deleteAll(anyList());
//         verify(userRoleRepository, never()).save(any(UserRole.class));
//     }

//     @Test
//     void testUpdateUserRoles_NoRolesProvided() {
//         Long userId = 1L;
//         List<Long> newRoleIds = Collections.emptyList();

//         User user = new User();
//         user.setId(userId);

//         when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//         when(userRoleRepository.findByUser_Id(userId)).thenReturn(Collections.emptyList());
//         when(roleService.findAllById(newRoleIds)).thenReturn(Collections.emptyList());

//         userService.updateUserRoles(userId, newRoleIds);

//         verify(userRoleRepository).deleteAll(Collections.emptyList());
//         verify(userRoleRepository, never()).save(any(UserRole.class));
//     }

//     @Test
//     void testUpdateUserRoles_ThrowsException_WhenUserNotFound() {
//         Long userId = 2L;
//         List<Long> newRoleIds = Arrays.asList(2L, 3L);
        
//         when(userRepository.findById(userId)).thenReturn(Optional.empty());

//         RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
//             userService.updateUserRoles(userId, newRoleIds);
//         });

//         assertEquals("User not found", thrown.getMessage());
//     }

//     @Test
//     void testDeleteUser_Success() {
//         when(userRepository.existsById(user.getId())).thenReturn(true);
        
//         userService.deleteUser(user.getId());
        
//         assertNull(userRepository.findById(user.getId()).orElse(null)); 
//         verify(userRepository, times(1)).deleteById(user.getId()); 
//     }

//     @Test
//     void testDeleteUser_ThrowsException_WhenUserNotFound() {
//         when(userRepository.existsById(user.getId())).thenReturn(false);
        
//         IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
//             userService.deleteUser(user.getId());
//         });

//         assertEquals("El usuario no existe.", thrown.getMessage());
//     }
// }