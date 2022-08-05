package com.example.demo.security.applicationuser.service;

import com.example.demo.security.applicationuser.repository.ApplicationUserRepository;
import com.example.demo.security.applicationuser.repository.entity.ApplicationUser;
import com.example.demo.security.role.repository.entity.Role;
import com.example.demo.security.rolegroup.repository.entity.RoleGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;

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
}
