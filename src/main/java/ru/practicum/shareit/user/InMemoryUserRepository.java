package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users;

    public User addUser(User user) {
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
            return user;
        }
    }

    public User getUser(long id) {
        try {
            Optional<User> userOpt = Optional.of(users.get(id));
            return userOpt.get();
        } catch (NullPointerException e) {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    public User updateUser(long id, User user) {
        User oldUser = getUser(id);
        users.remove(id);
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
            users.put(id, oldUser);
            return oldUser;
        }
    }

    public void deleteUser(long id) {
        users.remove(id);
    }
}
