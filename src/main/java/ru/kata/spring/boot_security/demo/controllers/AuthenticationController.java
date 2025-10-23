package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    // Получение информации о текущем аутентифицированном пользователе
    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        User user = userService.findByUsername(principal.getName());
        if (user != null) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());
            userData.put("age", user.getAge());
            userData.put("roles", user.getRoles().stream()
                    .map(role -> role.getName().replace("ROLE_", ""))
                    .collect(Collectors.toList()));
            return ResponseEntity.ok(userData);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "User not found"));
    }

    // Проверка аутентификации
    @GetMapping("/check")
    public ResponseEntity<?> checkAuthentication(Principal principal) {
        Map<String, Object> response = new HashMap<>();

        if (principal != null) {
            response.put("authenticated", true);
            response.put("username", principal.getName());

            User user = userService.findByUsername(principal.getName());
            if (user != null) {
                response.put("email", user.getEmail());
                response.put("age", user.getAge());
                response.put("roles", user.getRoles().stream()
                        .map(role -> role.getName().replace("ROLE_", ""))
                        .collect(Collectors.toList()));
            }
        } else {
            response.put("authenticated", false);
        }

        return ResponseEntity.ok(response);
    }

    // Выход из системы
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logged out successfully");
        return ResponseEntity.ok(responseBody);
    }

    // Получение ролей текущего пользователя
    @GetMapping("/roles")
    public ResponseEntity<?> getUserRoles(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        User user = userService.findByUsername(principal.getName());
        if (user != null) {
            Map<String, Object> rolesData = new HashMap<>();
            rolesData.put("username", user.getUsername());
            rolesData.put("roles", user.getRoles().stream()
                    .map(role -> role.getName().replace("ROLE_", ""))
                    .collect(Collectors.toList()));
            return ResponseEntity.ok(rolesData);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "User not found"));
    }

    // Проверка прав администратора
    @GetMapping("/is-admin")
    public ResponseEntity<?> checkAdminRole(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(Map.of("isAdmin", false));
        }

        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user != null && user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        return ResponseEntity.ok(Map.of("isAdmin", isAdmin));
    }

    // Получение публичной информации о пользователе (без чувствительных данных)
    @GetMapping("/user/{id}/public")
    public ResponseEntity<?> getPublicUserInfo(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            if (user != null) {
                Map<String, Object> publicInfo = new HashMap<>();
                publicInfo.put("id", user.getId());
                publicInfo.put("username", user.getUsername());
                publicInfo.put("email", user.getEmail());
                publicInfo.put("age", user.getAge());
                return ResponseEntity.ok(publicInfo);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error retrieving user info: " + e.getMessage()));
        }
    }
}
