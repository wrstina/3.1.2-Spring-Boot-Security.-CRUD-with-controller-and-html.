package ru.kata.spring.boot_security.demo.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.entities.User;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsSecurity implements UserDetails {

    private final User user;

    public UserDetailsSecurity(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // поток из ролей и преобразвание каждой роли в объект с заданным правом доступа
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    } // возвращает коллекцию всех юзера с правами доступа

    @Override
    public String getPassword() {return user.getPassword();}

    @Override
    public String getUsername() {return user.getUsername();}

    @Override
    public boolean isAccountNonExpired() {return user.isAccountNonExpired();}

    @Override
    public boolean isAccountNonLocked () {return user.isAccountNonLocked();}

    @Override public boolean isCredentialsNonExpired () {return user.isCredentialsNonExpired();}

    @Override
    public boolean isEnabled () {return user.isEnabled();}

    public User getUser() {
        return this.user;
    }
}
