package ru.kata.spring.boot_security.demo.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) { // <-- именно этот класс
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/css/**", "/js/**", "/webjars/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

    /* @Bean
    public JdbcUserDetailsService jdbcUserDetailsService(DataSource dataSource) {
        UserDetails user = User.builder()
                .username("user")
                .password("{bcrypt} user") //
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt} admin") //
                .roles("ADMIN", "USER")
                .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource)
        if(jdbcUserDetailsService.userExsists(user.getUsername()))

            {
                jdbcUserDetailsService.deleteUser(user.getUsername());
            }
        if(jdbcUserDetailsService.userExsists(admin.getUsername()))

            {
                jdbcUserDetailsService.deleteUser(admin.getUsername());
            }
        users.createUser(user);
        users.createUser(admin);
        return jdbcUserDetailsService;
    }
}




    /* аутентификация inMemory
    @Bean
    @Override
    public UserDetailsService userDetailsService() { // минимальная информация о пользователях
        UserDetails user = User.builder()
                        .username("user")
                        .password("{bcrypt} user") //
                        .roles("USER")
                        .build();

        UserDetails admin = User.builder()
                        .username("admin")
                        .password("{bcrypt} admin") //
                        .roles("ADMIN", "USER")
                        .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}

@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/authenticated/**").authenticated()
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/only_for_admins/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler)// обработка зашедшего пользователя
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/")
                .permitAll();
    }
     */

