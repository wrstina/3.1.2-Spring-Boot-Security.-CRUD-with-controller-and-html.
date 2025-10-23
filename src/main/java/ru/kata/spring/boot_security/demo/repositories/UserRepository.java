package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kata.spring.boot_security.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Без fetch (обычный поиск)
    Optional<User> findByUsername(String username);

    // С ролями, чтобы не было N+1
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(@Param("username") String username);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles")
    List<User> findAllWithRoles();

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") Long id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
