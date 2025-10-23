package ru.kata.spring.boot_security.demo.services;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.entities.Role;

import java.util.List;

public interface RoleService  {
    List<Role> findAll();
    Role findById(Long id);
    Role findByName(String name);
    Role save(Role role);
    Role createRole(String roleName);
    void deleteById(Long id);
    void initializeDefaultRoles();
    List<Role> findRolesByIds(List<Long> roleIds);
    List<Role> findAllById(Iterable<Long> ids);
    boolean existsByName(String name);
    Role findOrCreateRole(String roleName);
    }
