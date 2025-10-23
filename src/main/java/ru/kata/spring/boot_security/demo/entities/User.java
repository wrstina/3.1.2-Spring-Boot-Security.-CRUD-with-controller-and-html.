package ru.kata.spring.boot_security.demo.entities;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @Column(nullable = false)
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Column(unique = true, nullable = false)
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Column(nullable = false)
    private int age;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired = true;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired = true;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }

    public boolean isEnabled() {return enabled;}

    public boolean isAccountNonExpired() {return accountNonExpired;}

    public boolean isAccountNonLocked() {return accountNonLocked;}

    public boolean isCredentialsNonExpired() {return credentialsNonExpired;}

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>(); // пользователь не может иметь две одинаковыные роли

    public User() {}// hiber
    public User(String username, String password, String email, int age) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
    }
    // геттеры и сеттеры для изменения данных через api
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public void setAge(int age) {this.age = age;}
    public int getAge() {return age;}

    public Set<Role> getRoles() {return roles;}
    public void setRoles(Set<Role> roles) {this.roles = roles;}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
