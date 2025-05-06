package com.trade.icesi_trade.Service.Interface;
import java.util.List;

import com.trade.icesi_trade.dtos.RegisterDto;
import com.trade.icesi_trade.dtos.UserResponseDto;
import com.trade.icesi_trade.model.User;

public interface UserService {
    User findUserByEmail(String email);
    User findUserById(Long id);
    List<User> findAllUsers();
    User saveUser(User user);
    void deleteUser(Long userId);
    UserResponseDto updateUser(UserResponseDto dto, Long id);
    void updateUserRoles(Long userId, List<Long> newRoleIds);
    User register(RegisterDto dto);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
}
