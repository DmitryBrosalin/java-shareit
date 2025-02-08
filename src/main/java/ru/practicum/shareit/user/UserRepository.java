package ru.practicum.shareit.user;

public interface UserRepository {
    User addUser(User user);

    User getUser(long id);

    User updateUser(long id, User user);

    void deleteUser(long id);
}
