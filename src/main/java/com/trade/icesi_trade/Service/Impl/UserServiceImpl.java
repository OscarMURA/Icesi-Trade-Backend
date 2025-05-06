package com.trade.icesi_trade.Service.Impl;

import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.Service.Interface.RoleService;
import com.trade.icesi_trade.dtos.RegisterDto;
import com.trade.icesi_trade.dtos.UserResponseDto;
import com.trade.icesi_trade.mappers.UserMapper;
import com.trade.icesi_trade.model.Role;
import com.trade.icesi_trade.model.User;
import com.trade.icesi_trade.model.UserRole;
import com.trade.icesi_trade.repository.UserRepository;
import com.trade.icesi_trade.repository.UserRoleRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        return userMapper.entityToDto(findUserById(id));
    }

    @Override
    @Transactional
    public User register(RegisterDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .phone(dto.getPhone())
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        Role defaultRole = roleService.findRoleByName("ROLE_USER");
        UserRole userRole = UserRole.builder()
                .user(savedUser)
                .role(defaultRole)
                .build();
        userRoleRepository.save(userRole);

        return savedUser;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado."));
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado."));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        if (user.getEmail() == null) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }

        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("Usuario no encontrado.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponseDto updateUser(UserResponseDto dto, Long id) {
        User existing = findUserById(id);
        existing.setEmail(dto.getEmail());
        existing.setName(dto.getName());
        existing.setPhone(dto.getPhone());
        existing.setUpdatedAt(LocalDateTime.now());
        User saved = userRepository.save(existing);
        return userMapper.entityToDto(saved);
    }

    @Override
    @Transactional
    public void updateUserRoles(Long userId, List<Long> newRoleIds) {
        User user = findUserById(userId);
        List<UserRole> currentRoles = userRoleRepository.findByUser_Id(userId);
        userRoleRepository.deleteAllInBatch(currentRoles);

        List<Role> roles = roleService.findAllById(newRoleIds.stream().distinct().toList());
        roles.forEach(role -> {
            UserRole ur = UserRole.builder()
                    .user(user)
                    .role(role)
                    .build();
            userRoleRepository.save(ur);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findUserByEmail(email);

        List<GrantedAuthority> authorities = user.getUserRoles().stream()
                .map(ur -> (GrantedAuthority) () -> ur.getRole().getName())
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}