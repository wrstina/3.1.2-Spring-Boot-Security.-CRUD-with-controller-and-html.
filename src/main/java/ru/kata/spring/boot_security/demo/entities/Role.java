package ru.kata.spring.boot_security.demo.entities;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // "ROLE_ADMIN", "ROLE_USER"

    public Role() {
    }
    public Role(String name) {this.name = name.startsWith("ROLE_") ? name : "ROLE_" + name;}

    @Override
    public String getAuthority() {return name;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    @Override
    public String toString() {return name;}

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
}
