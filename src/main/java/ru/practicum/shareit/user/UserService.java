package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    public User getUser(long id) {
        return userRepository.getUser(id);
    }

    public User updateUser(long id, User user) {
        return userRepository.updateUser(id, user);
    }

    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }
}
