package com.trade.icesi_trade.Service.Interface;
import java.util.List;

import com.trade.icesi_trade.model.User;

public interface UserService {
    User findUserByEmail(String email);
    User saveUser(User user);
    void deleteUser(Long userId);
    User updateUser(User user, Long id);
    void updateUserRoles(Long userId, List<Long> newRoleIds);

}
