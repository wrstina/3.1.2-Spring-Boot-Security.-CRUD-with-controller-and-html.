package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // <—
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DTOs.UserUpdateRequest;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAllWithRoles();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findByIdWithRoles(id).orElse(null);
    }

    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsernameWithRoles(username).orElse(null);
    }

    @Override
    @Transactional
    public User updateFromRequest(Long id, UserUpdateRequest updateRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (updateRequest.getUsername() != null) {
            existingUser.setUsername(updateRequest.getUsername());
        }
        if (updateRequest.getEmail() != null) {
            existingUser.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getAge() != null) {
            existingUser.setAge(updateRequest.getAge());
        }
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public User save(User user) {
        if (!isPasswordEncoded(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private boolean isPasswordEncoded(String password) {
        return password != null &&
                (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"));
    }
}