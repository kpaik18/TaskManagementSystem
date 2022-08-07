package com.example.demo.security.applicationuser.service;

import com.example.demo.exception.BusinessLogicException;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public ApplicationUser lookupUser(Long id) {
        Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findById(id);
        if (applicationUserOptional.isEmpty()) {
            throw new SecurityException();
        }
        return applicationUserOptional.get();
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
                u.getRoles(),
                u.getRoleGroups())).collect(Collectors.toList());
    }

    public ApplicationUserWithPassword registerApplicationUser(ApplicationUser user) {
        String randomPassword = PasswordGenerator.generatePassword(16);
        String encodedPassword = bCryptPasswordEncoder.encode(randomPassword);
        user.setPassword(encodedPassword);
        try {
            applicationUserRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("uk_sec_user_username")) {
                throw new BusinessLogicException("username already exists");
            }
            throw new BusinessLogicException("persistence exception");
        }
        return new ApplicationUserWithPassword(user.getId(), user.getUsername(), randomPassword);

    }

    public void updateApplicationUser(Long id, ApplicationUser user) {
        if (id == 1) {
            throw new SecurityException();
        }
        ApplicationUser dbUser = lookupUser(id);
        dbUser.setRoles(user.getRoles());
        dbUser.setRoleGroups(user.getRoleGroups());
        dbUser.setUsername(user.getUsername());
        try {
            applicationUserRepository.saveAndFlush(dbUser);
        } catch (DataIntegrityViolationException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void deleteApplicationUser(Long id) {
        if (id == 1) {
            throw new SecurityException();
        }
        applicationUserRepository.deleteById(id);
    }

    public ApplicationUserDTO getApplicationUser(Long id) {
        if (id == 1) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<ApplicationUser> viewer = applicationUserRepository.findByUsername(username);
            if (viewer.isEmpty() || viewer.get().getId() != 1) {
                throw new SecurityException("cant see admin data");
            }
        }
        ApplicationUser dbUser = lookupUser(id);
        return new ApplicationUserDTO(dbUser.getId(),
                dbUser.getUsername(),
                dbUser.getRoles(),
                dbUser.getRoleGroups());
    }

    public List<ApplicationUserDTO> getAllApplicationUserDTOs() {
        List<ApplicationUser> allUsers = applicationUserRepository.findAll();
        return allUsers.stream().map(u -> new ApplicationUserDTO(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
    }
}
