package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users;

    public UserDto addUser(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BadRequestException("Email и имя пользователя не могут быть пустыми.");
        }
        boolean alreadyUserEmail = users.values().stream()
                .map(User::getEmail)
                .anyMatch(userEmail -> userEmail.equals(user.getEmail()));
        if (alreadyUserEmail) {
            throw new InternalServerException("Email уже используется.");
        } else {
            user.setId(users.size() + 1);
            users.put(user.getId(), user);
            return UserMapper.toUserDto(user);
        }
    }

    public User getUser(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    public UserDto updateUser(long id, User user) {
        User oldUser = getUser(id);
        boolean alreadyUserEmail = users.values().stream()
                .map(User::getEmail)
                .anyMatch(userEmail -> userEmail.equals(user.getEmail()));
        if (alreadyUserEmail) {
            throw new InternalServerException("Email уже используется.");
        } else {
            user.setId(id);
            if (user.getName() != null && !user.getName().isBlank()) {
                oldUser.setName(user.getName());
            }
            if (user.getEmail() != null && !user.getEmail().isBlank()) {
                oldUser.setEmail(user.getEmail());
            }
            return UserMapper.toUserDto(oldUser);
        }
    }

    public void deleteUser(long id) {
        users.remove(id);
    }
}
