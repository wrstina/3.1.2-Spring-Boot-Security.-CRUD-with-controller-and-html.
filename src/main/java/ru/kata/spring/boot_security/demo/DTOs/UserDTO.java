package ru.kata.spring.boot_security.demo.DTOs;

import ru.kata.spring.boot_security.demo.entities.Role;

import java.util.Set;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Integer age;
    private Set<Role> roles;
}
