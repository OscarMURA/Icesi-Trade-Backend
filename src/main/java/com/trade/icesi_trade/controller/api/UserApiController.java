package com.trade.icesi_trade.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.trade.icesi_trade.Service.Impl.JwtServiceImpl;
import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.Service.Interface.RoleService;
import com.trade.icesi_trade.dtos.RegisterDto;
import com.trade.icesi_trade.dtos.UserResponseDto;
import com.trade.icesi_trade.dtos.RoleDto;
import com.trade.icesi_trade.mappers.UserMapper;
import com.trade.icesi_trade.mappers.RoleMapper;
import com.trade.icesi_trade.model.User;
import com.trade.icesi_trade.model.Role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = { "${app.frontend.url}", "${app.frontend.alternative-url}" })
@Tag(name = "Users", description = "CRUD operations for users")
public class UserApiController {

        @Autowired
        private UserService userService;

        @Autowired
        private RoleService roleService;

        @Autowired
        private UserMapper userMapper;

        @Autowired
        private RoleMapper roleMapper;

        @Operation(summary = "Get all users", description = "Retrieve a list of all registered users. You can optionally filter users by role.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid role filter")
        })

        @GetMapping
        public ResponseEntity<List<UserResponseDto>> getAllUsers(
                        @Parameter(description = "Optional role name to filter users") @RequestParam(required = false) String roleName) {
                List<User> users = userService.findAllUsers();

                if (roleName != null && !roleName.isBlank()) {
                        users = users.stream()
                                        .filter(user -> user.getUserRoles().stream()
                                                        .anyMatch(ur -> ur.getRole().getName()
                                                                        .equalsIgnoreCase(roleName)))
                                        .toList();
                }

                List<UserResponseDto> result = users.stream()
                                .map(userMapper::entityToDto)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(result);
        }

        @Operation(summary = "Get user by ID", description = "Retrieve a user using their unique ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User found"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })

        @GetMapping("/{id}")
        public ResponseEntity<UserResponseDto> getUserById(
                        @Parameter(description = "User ID", required = true) @PathVariable Long id) {
                User user = userService.findUserById(id);
                return ResponseEntity.ok(userMapper.entityToDto(user));
        }

        @Operation(summary = "Register new user", description = "Create a new user with the provided information")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "User registered successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data or user already exists")
        })
        @PostMapping
        public ResponseEntity<UserResponseDto> createUser(
                        @Parameter(description = "User registration data", required = true) @Valid @RequestBody RegisterDto dto) {
                User createdUser = userService.register(dto);
                return new ResponseEntity<>(userMapper.entityToDto(createdUser), HttpStatus.CREATED);
        }

        @Operation(summary = "Update existing user", description = "Update the information of an existing user")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User updated successfully"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @PutMapping("/{id}")
        public ResponseEntity<UserResponseDto> updateUser(
                        @Parameter(description = "ID of the user to update", required = true) @PathVariable Long id,
                        @Parameter(description = "Updated user data", required = true) @RequestBody UserResponseDto user) {
                User updated = userService.updateUser(userMapper.dtoToEntity(user), id);
                return ResponseEntity.ok(userMapper.entityToDto(updated));
        }

        @Operation(summary = "Delete user by ID", description = "Remove a user from the system using their ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<String> deleteUser(
                        @Parameter(description = "ID of the user to delete", required = true) @PathVariable Long id) {
                userService.deleteUser(id);
                return ResponseEntity.ok("User with ID " + id + " deleted successfully.");
        }

        @DeleteMapping("/email/{email}")
        public ResponseEntity<String> deleteByEmail(@PathVariable String email) {
                User user = userService.findUserByEmail(email);
                userService.deleteUser(user.getId());
                return ResponseEntity.ok("Usuario eliminado con email: " + email);
        }

        @Operation(summary = "Get user roles")
        @GetMapping("/{id}/roles")
        public ResponseEntity<List<RoleDto>> getUserRoles(@PathVariable Long id) {
                User user = userService.findUserById(id);
                List<RoleDto> roles = user.getUserRoles().stream()
                                .map(ur -> roleMapper.entityToDto(ur.getRole()))
                                .collect(Collectors.toList());
                return ResponseEntity.ok(roles);
        }

        @Operation(summary = "Update user roles")
        @PostMapping("/{id}/roles")
        public ResponseEntity<String> updateUserRoles(
                        @PathVariable Long id,
                        @RequestBody List<Long> roleIds) {
                if (roleIds == null || roleIds.isEmpty()) {
                        return ResponseEntity.badRequest().body("Debe asignar al menos un rol");
                }

                userService.updateUserRoles(id, roleIds);
                return ResponseEntity.ok("Roles updated successfully");
        }
}
