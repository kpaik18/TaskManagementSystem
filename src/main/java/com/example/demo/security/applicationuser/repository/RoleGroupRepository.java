package com.example.demo.security.applicationuser.repository;

import com.example.demo.security.applicationuser.repository.entity.RoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleGroupRepository extends JpaRepository<RoleGroup, Long> {
}
