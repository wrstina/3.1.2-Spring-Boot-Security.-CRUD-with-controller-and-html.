package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.DTOs.UserUpdateRequest;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    List<User> findAll();
    User findById(Long id);
    User findByUsername(String username);
    User updateFromRequest(Long id, UserUpdateRequest updateRequest);
    User save(User user);
    void deleteById(Long id);
}