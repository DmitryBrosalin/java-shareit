package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class User {
    private long id;
    private String name;
    @Email
    private String email;
}
