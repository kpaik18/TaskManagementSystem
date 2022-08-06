package com.example.demo.security.applicationuser.service;

import com.example.demo.security.applicationuser.controller.dto.ApplicationUserDTO;
import com.example.demo.security.applicationuser.controller.dto.ApplicationUserWithPassword;
import com.example.demo.security.applicationuser.repository.ApplicationUserRepository;
import com.example.demo.security.applicationuser.repository.RoleGroupRepository;
import com.example.demo.security.applicationuser.repository.RoleRepository;
import com.example.demo.security.applicationuser.repository.entity.ApplicationUser;
import com.example.demo.security.applicationuser.repository.entity.Role;
import com.example.demo.security.applicationuser.repository.entity.RoleGroup;
import com.example.demo.security.applicationuser.service.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;
    private final RoleRepository roleRepository;
    private final RoleGroupRepository roleGroupRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Optional<ApplicationUser> getUserByUsername(String username) {
        return applicationUserRepository.findByUsername(username);
    }

    public Set<Role> getAllRolesForUser(ApplicationUser user) {
        Set<Role> combinedRoles = new HashSet<>();

        List<Role> userRoles = user.getRoles();
        List<RoleGroup> userRoleGroups = user.getRoleGroups();

        combinedRoles.addAll(userRoles);

        for (RoleGroup roleGroup : userRoleGroups) {
            combinedRoles.addAll(roleGroup.getRoles());
        }
        return combinedRoles;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ApplicationUser> userOptional = applicationUserRepository.findByUsername(username);
        if (username.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }
        ApplicationUser user = userOptional.get();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        Set<Role> userRoles = getAllRolesForUser(user);
        userRoles.stream()
                .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<RoleGroup> getAllRoleGroups() {
        return roleGroupRepository.findAll();
    }

    public List<ApplicationUserDTO> getAllApplicationUsers() {
        return applicationUserRepository.findAll().stream().map(u -> new ApplicationUserDTO(u.getId(),
                u.getUsername(),
                u.getIsPasswordReset(),
                u.getRoles(),
                u.getRoleGroups())).collect(Collectors.toList());
    }

    public ApplicationUserWithPassword registerApplicationUser(ApplicationUser user) {
        user.setIsPasswordReset(true);
        String randomPasswordForUser = PasswordGenerator.generatePassword(16);
        user.setPassword(bCryptPasswordEncoder.encode(randomPasswordForUser));
        try {
            applicationUserRepository.saveAndFlush(user);
        }catch (DataIntegrityViolationException ex){
            if(ex.getMessage().contains("uk_sec_user_username")){
                throw new RuntimeException("username already exists");
            }
            throw new RuntimeException("persistence exception");
        }
        return new ApplicationUserWithPassword(user.getId(), user.getUsername(), randomPasswordForUser);

    }

}
