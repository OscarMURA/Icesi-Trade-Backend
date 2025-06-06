package com.trade.icesi_trade.controller.api;

import com.trade.icesi_trade.Service.Interface.RoleService;
import com.trade.icesi_trade.dtos.RoleDto;
import com.trade.icesi_trade.mappers.RoleMapper;
import com.trade.icesi_trade.model.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin
@Tag(name = "Roles", description = "CRUD operations for roles")
public class RoleApiController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @Operation(summary = "Get all roles")
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.findAllRoles().stream()
                .map(roleMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Create new role")
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleDto roleDto) {
        Role role = roleMapper.dtoToEntity(roleDto);
        Role savedRole = roleService.saveRole(role, List.of());
        return ResponseEntity.ok(roleMapper.entityToDto(savedRole));
    }

    @Operation(summary = "Update role")
    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleDto roleDto) {
        Role role = roleMapper.dtoToEntity(roleDto);
        Role updatedRole = roleService.updateRole(id, role);
        return ResponseEntity.ok(roleMapper.entityToDto(updatedRole));
    }

    @Operation(summary = "Delete role")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok("Rol eliminado exitosamente");
    }

    @Operation(summary = "Get role by ID")
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id) {
        return roleService.findById(id)
                .map(roleMapper::entityToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}