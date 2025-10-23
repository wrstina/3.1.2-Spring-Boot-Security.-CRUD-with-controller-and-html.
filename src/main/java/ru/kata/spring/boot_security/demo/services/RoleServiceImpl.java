package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.orElse(null);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role createRole(String roleName) {
        String formattedRoleName = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName.toUpperCase();

        Role existingRole = findByName(formattedRoleName);
        if (existingRole != null) {
            return existingRole;
        }

        Role role = new Role(formattedRoleName);
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void initializeDefaultRoles() {
        createRole("ADMIN");
        createRole("USER");
    }

    @Override
    public List<Role> findRolesByIds(List<Long> roleIds) {
        return roleRepository.findAllById(roleIds);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.findByName(name) != null;
    }

    @Override
    @Transactional
    public Role findOrCreateRole(String roleName) {
        Role role = findByName(roleName);
        if (role == null) {
            role = createRole(roleName);
        }
        return role;
    }
}
