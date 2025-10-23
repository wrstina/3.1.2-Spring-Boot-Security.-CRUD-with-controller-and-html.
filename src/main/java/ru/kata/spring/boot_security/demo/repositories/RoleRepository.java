package ru.kata.spring.boot_security.demo.repositories;

import ru.kata.spring.boot_security.demo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
