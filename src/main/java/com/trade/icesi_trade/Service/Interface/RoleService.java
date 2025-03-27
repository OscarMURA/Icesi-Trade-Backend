package com.trade.icesi_trade.Service.Interface;

import com.trade.icesi_trade.model.Role;

public interface RoleService {
    Role findRoleByName(String name);
    Role saveRole(Role role);
    void deleteRole(Long roleId);
}
