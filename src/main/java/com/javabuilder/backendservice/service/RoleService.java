package com.javabuilder.backendservice.service;

import com.javabuilder.backendservice.common.RoleName;
import com.javabuilder.backendservice.entity.Role;

public interface RoleService {
    Role findByOrCreate(RoleName roleName);
}
