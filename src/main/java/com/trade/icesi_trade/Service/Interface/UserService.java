package com.trade.icesi_trade.Service.Interface;
import com.trade.icesi_trade.model.User;

public interface UserService {
    User findUserByEmail(String email);
    User saveUser(User user);
    void deleteUser(Long userId);
}
