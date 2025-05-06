package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.dtos.RegisterDto;
import com.trade.icesi_trade.dtos.UserResponseDto;
import com.trade.icesi_trade.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations related to users")
public class UserApiController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid role filter")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
        @Parameter(description = "Optional role name to filter users")
        @RequestParam(required = false) String roleName
    ) {
        List<User> users = userService.findAllUsers();

        if (roleName != null && !roleName.isBlank()) {
            users = users.stream()
                .filter(user -> user.getUserRoles().stream()
                    .anyMatch(ur -> ur.getRole().getName().equalsIgnoreCase(roleName)))
                .toList();
        }

        List<UserResponseDto> userDtos = users.stream()
            .map(user -> userService.getUserById(user.getId()))
            .toList();

        return ResponseEntity.ok(userDtos);
    }   

    @Operation(summary = "Get user by ID", description = "Retrieve a user using their unique ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Register new user", description = "Create a new user with the provided information")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or user already exists")
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
        @Valid @RequestBody RegisterDto dto
    ) {
        User created = userService.register(dto);
        return new ResponseEntity<>(userService.getUserById(created.getId()), HttpStatus.CREATED);
    }

    @Operation(summary = "Update existing user", description = "Update only the email, name, password and phone fields of a user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
        @Parameter(description = "ID of the user to update", required = true)
        @PathVariable Long id,
        @Parameter(description = "User fields to update", required = true)
        @RequestBody UserResponseDto userDto
    ) {
        UserResponseDto updated = userService.updateUser(userDto, id);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete user by ID", description = "Remove a user from the system using their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}