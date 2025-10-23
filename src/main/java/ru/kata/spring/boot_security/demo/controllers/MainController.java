package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.services.RoleService; // <-- не забудь импорт

import java.security.Principal;

@Controller
public class MainController {

    private final UserService userService;
    private final RoleService roleService;

    // Конструкторное внедрение без Lombok
    public MainController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String starterPage() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String homePage() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String pageForReadProfile() {
        return "user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("users", userService.findAll());       // список пользователей для таблицы
        model.addAttribute("allRoles", roleService.findAll());    // список ролей для модалки
        if (principal != null) {
            model.addAttribute("currentUser",
                    userService.findByUsername(principal.getName())); // текущий админ (опционально)
        }
        return "admin";
    }
}
