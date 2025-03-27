package com.trade.icesi_trade.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.icesi_trade.Service.Interface.UserService;
import com.trade.icesi_trade.model.User;
import com.trade.icesi_trade.repository.UserRepository;
import com.trade.icesi_trade.repository.UserRoleRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired

    private  UserRoleRepository userRoleRepository;

    

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        
        if (user.getId() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("El usuario debe tener al menos un ID y un email.");
        }
        if (userRoleRepository.countByUser_Id(user.getId()) == 0) {
            throw new IllegalArgumentException("El usuario debe tener al menos un rol asignado.");
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
