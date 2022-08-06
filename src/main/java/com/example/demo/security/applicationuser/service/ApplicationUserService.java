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

    private ApplicationUser lookupUser(Long id){
        Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findById(id);
        if(applicationUserOptional.isEmpty()){
            throw new RuntimeException("user does not exist");
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
                u.getIsPasswordReset(),
                u.getRoles(),
                u.getRoleGroups())).collect(Collectors.toList());
    }

    public ApplicationUserWithPassword registerApplicationUser(ApplicationUser user) {
        user.setIsPasswordReset(true);
        String randomPassword = PasswordGenerator.generatePassword(16);
        user.setPassword(bCryptPasswordEncoder.encode(randomPassword));
        try {
            applicationUserRepository.saveAndFlush(user);
        }catch (DataIntegrityViolationException ex){
            if(ex.getMessage().contains("uk_sec_user_username")){
                throw new RuntimeException("username already exists");
            }
            throw new RuntimeException("persistence exception");
        }
        return new ApplicationUserWithPassword(user.getId(), user.getUsername(), randomPassword);

    }

    public void updateApplicationUser(Long id, ApplicationUser user) {
        ApplicationUser dbUser = lookupUser(id);
        dbUser.setRoles(user.getRoles());
        dbUser.setRoleGroups(user.getRoleGroups());
        dbUser.setUsername(user.getUsername());
        try{
            applicationUserRepository.saveAndFlush(dbUser);
        }catch (DataIntegrityViolationException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    public ApplicationUserWithPassword resetUserPassword(Long id) {
        ApplicationUser dbUser = lookupUser(id);
        String randomPassword = PasswordGenerator.generatePassword(16);
        dbUser.setPassword(bCryptPasswordEncoder.encode(randomPassword));
        dbUser.setIsPasswordReset(true);
        applicationUserRepository.save(dbUser);
        return new ApplicationUserWithPassword(id, dbUser.getUsername(), randomPassword);
    }

    public void deleteApplicationUser(Long id) {
        applicationUserRepository.deleteById(id);
    }

    public ApplicationUserDTO getApplicationUser(Long id) {
        ApplicationUser dbUser = lookupUser(id);
        return new ApplicationUserDTO(dbUser.getId(),
                dbUser.getUsername(),
                dbUser.getIsPasswordReset(),
                dbUser.getRoles(),
                dbUser.getRoleGroups());
    }
}
