package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTOs.UserUpdateRequest;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private UserService userService;
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();

        List<Map<String, Object>> userDTOs = users.stream()
                .map(user -> {
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("id", user.getId());
                    userData.put("username", user.getUsername());
                    userData.put("email", user.getEmail());
                    userData.put("age", user.getAge());
                    userData.put("roles", user.getRoles());
                    return userData;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            User updatedUser = userService.updateFromRequest(id, userUpdateRequest);
            return ResponseEntity.ok("User updated successfully: " + updatedUser.getUsername());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting user: " + e.getMessage());
        }
    }
}
