package com.trade.icesi_trade.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.icesi_trade.Service.Interface.PermissionService;
import com.trade.icesi_trade.model.Permission;
import com.trade.icesi_trade.repository.PermissionRepository;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission findPermissionByName(String name) {
        return permissionRepository.findByName(name);
    }

    @Override
    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(Long permissionId) {
        permissionRepository.deleteById(permissionId);
    }
}
