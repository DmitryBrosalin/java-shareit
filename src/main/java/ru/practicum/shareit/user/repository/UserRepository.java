package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    UserDto addUser(User user);

    User getUser(long id);

    UserDto updateUser(long id, User user);

    void deleteUser(long id);
}
