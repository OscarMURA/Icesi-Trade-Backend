package com.trade.icesi_trade.Service.Interface;

import java.util.List;

import com.trade.icesi_trade.model.Role;

public interface RoleService {
    Role findRoleByName(String name);
    Role saveRole(Role role);
    void deleteRole(Long roleId);
    List<Role> findAllRoles();
    List<Role> findAllById(List<Long> ids);
}
