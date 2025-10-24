package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("userForm", new User());
        if (principal != null) {
            model.addAttribute("currentUser", userService.findByUsername(principal.getName()));
        }
        return "admin";
    }

    @PostMapping("/admin/users")
    public String create(@ModelAttribute("userForm") User user,
                         @RequestParam(name = "roleIds", required = false) List<Long> roleIds) {
        Set<Role> roles = roleIds == null || roleIds.isEmpty()
                ? new HashSet<>()
                : new HashSet<>(roleService.findAllById(roleIds));
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/users/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute User user,
                         @RequestParam(name = "roleIds", required = false) List<Long> roleIds) {
        user.setId(id);
        Set<Role> roles = roleIds == null || roleIds.isEmpty()
                ? Collections.emptySet()
                : new HashSet<>(roleService.findAllById(roleIds));
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/users/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
