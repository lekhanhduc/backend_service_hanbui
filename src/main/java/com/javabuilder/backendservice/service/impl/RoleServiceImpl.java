package com.javabuilder.backendservice.service.impl;

import com.javabuilder.backendservice.common.RoleName;
import com.javabuilder.backendservice.entity.Role;
import com.javabuilder.backendservice.repository.RoleRepository;
import com.javabuilder.backendservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByOrCreate(RoleName roleName) {
        return roleRepository.findById(roleName.name())
                .orElseGet(() -> {
                    Role role = Role.builder()
                            .name(roleName.name())
                            .build();
                    return roleRepository.save(role);
                });
    }
}
