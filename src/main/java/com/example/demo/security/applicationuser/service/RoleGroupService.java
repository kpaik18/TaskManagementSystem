package com.example.demo.security.applicationuser.service;

import com.example.demo.security.applicationuser.repository.RoleGroupRepository;
import com.example.demo.security.applicationuser.repository.entity.RoleGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleGroupService {
    private final RoleGroupRepository roleGroupRepository;

    public List<RoleGroup> getAllRoleGroups() {
        return roleGroupRepository.findAll();
    }

    public RoleGroup lookupRoleGroup(Long id) {
        Optional<RoleGroup> roleGroupOptional = roleGroupRepository.findById(id);
        if(roleGroupOptional.isEmpty()){
            throw new RuntimeException();
        }
        return roleGroupOptional.get();
    }

    public RoleGroup createRoleGroup(RoleGroup roleGroup) {
        try {
            roleGroup = roleGroupRepository.saveAndFlush(roleGroup);
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return roleGroup;
    }

    public void updateRoleGroup(Long id, RoleGroup roleGroup) {
        RoleGroup dbRoleGroup = lookupRoleGroup(id);
        dbRoleGroup.setRoleGroupName(roleGroup.getRoleGroupName());
        dbRoleGroup.setRoles(roleGroup.getRoles());
        try{
            roleGroupRepository.saveAndFlush(dbRoleGroup);
        }catch (DataIntegrityViolationException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void deleteRoleGroup(Long id) {
        roleGroupRepository.deleteById(id);
    }

    public RoleGroup getRoleGroup(Long id) {
        return lookupRoleGroup(id);
    }
}
