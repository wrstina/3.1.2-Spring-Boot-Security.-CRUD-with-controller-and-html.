package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTOs.UserUpdateRequest;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
    public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Возвращаем DTO без пароля и с нормальной сериализацией ролей
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", userPrincipal.getId());
        userData.put("username", userPrincipal.getUsername());
        userData.put("email", userPrincipal.getEmail());
        userData.put("age", userPrincipal.getAge());
        userData.put("roles", userPrincipal.getRoles().stream()
                .map(role -> {
                    Map<String, Object> roleData = new HashMap<>();
                    roleData.put("id", role.getId());
                    roleData.put("name", role.getName());
                    return roleData;
                })
                .collect(Collectors.toList()));

        return ResponseEntity.ok(userData);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal User currentUser,
                                           @RequestBody UserUpdateRequest updates) {
        try {
            User updatedUser = userService.updateFromRequest(currentUser.getId(), updates);

            Map<String, Object> response = new HashMap<>();
            response.put("id", updatedUser.getId());
            response.put("username", updatedUser.getUsername());
            response.put("email", updatedUser.getEmail());
            response.put("age", updatedUser.getAge());
            response.put("roles", updatedUser.getRoles().stream()
                    .map(role -> {
                        Map<String, Object> roleData = new HashMap<>();
                        roleData.put("id", role.getId());
                        roleData.put("name", role.getName());
                        return roleData;
                    })
                    .collect(Collectors.toList()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating profile: " + e.getMessage());
        }
    }
}


