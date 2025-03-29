package com.trade.icesi_trade.Service.Impl;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.model.Role;
import com.trade.icesi_trade.model.User;
import com.trade.icesi_trade.model.UserRole;
import com.trade.icesi_trade.repository.UserRepository;
import com.trade.icesi_trade.repository.UserRoleRepository;

import jakarta.transaction.Transactional;
  
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  UserRoleRepository userRoleRepository;

    @Autowired
    private  RoleServiceImpl roleService;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado."));
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado."));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        
        if (user.getId() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("El usuario debe tener al menos un ID y un email.");
        }
        if (userRoleRepository.countByUser_Id(user.getId()) == 0) {
            throw new IllegalArgumentException("El usuario debe tener al menos un rol asignado.");
        }
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(User user, Long id) {
        if (user.getId() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("El usuario debe tener al menos un ID y un email.");
        }
        User existingUser = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado."));
        existingUser.setEmail(user.getEmail());
        existingUser.setName(user.getName());
        existingUser.setPhone(user.getPhone());
        existingUser.setPassword(user.getPassword());
        existingUser.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(existingUser);
    }

    @Transactional
    public void updateUserRoles(Long userId, List<Long> newRoleIds) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserRole> existingRoles = userRoleRepository.findByUser_Id(userId);
        userRoleRepository.deleteAll(existingRoles);

        List<Role> newRoles =  roleService.findAllById(newRoleIds);
        for (Role role : newRoles) {
            UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();
            userRoleRepository.save(userRole);
        }
    }
}