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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = applicationUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        List<Role> userRoles = user.getRoles();
        List<RoleGroup> userRoleGroups = user.getRoleGroups();

        Set<Role> combinedRoles = userRoles.stream().collect(Collectors.toSet());
        for (RoleGroup roleGroup : userRoleGroups) {
            combinedRoles.addAll(roleGroup.getRoles());
        }
        combinedRoles.stream()
                .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
