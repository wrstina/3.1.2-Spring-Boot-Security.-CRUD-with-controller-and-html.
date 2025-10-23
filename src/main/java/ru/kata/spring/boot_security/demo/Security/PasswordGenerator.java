package ru.kata.spring.boot_security.demo.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("Punya: " + encoder.encode("punya"));
        System.out.println("Pudge: " + encoder.encode("pudge"));
    }
}

