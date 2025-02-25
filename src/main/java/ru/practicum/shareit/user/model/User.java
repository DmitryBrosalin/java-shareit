package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class User {
    private long id;
    private String name;
    @Email
    private String email;

    public User(String name, String email) {
        this.setName(name);
        this.setEmail(email);
    }
}

