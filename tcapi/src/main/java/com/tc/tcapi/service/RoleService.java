package com.tc.tcapi.service;

import com.tc.core.enumm.ERole;
import com.tc.tcapi.model.Role;
import com.tc.tcapi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepo;

    public Role save(Role role) {
        Role rs = roleRepo.saveAndFlush(role);
        return rs;
    }

    public Role getMemberRole() {
        return roleRepo.findByName(ERole.ROLE_MEMBER).orElse(null);
    }

    public Role getAdminRole() {
        return roleRepo.findByName(ERole.ROLE_ADMIN).orElse(null);
    }

}
