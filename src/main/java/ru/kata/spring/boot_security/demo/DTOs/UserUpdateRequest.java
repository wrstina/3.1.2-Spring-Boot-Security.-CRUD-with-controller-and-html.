package ru.kata.spring.boot_security.demo.DTOs;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class UserUpdateRequest {

    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    @Min(18) @Max(120)
    private Integer age;

    @Size(min = 3, message = "Password must be at least 3 characters")
    private String password;


    public UserUpdateRequest() {} // hiber
    public UserUpdateRequest(String username, String email, Integer age, String password) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public Integer getAge() {return age;}
    public void setAge(Integer age) {this.age = age;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    @Override
    public String toString() {
        return "UserUpdateRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", password='***'" +
                '}';
    }
}



