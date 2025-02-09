package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.fromUserDto(userDto);
        return userRepository.addUser(user);
    }

    public UserDto getUser(long id) {
        return UserMapper.toUserDto(userRepository.getUser(id));
    }

    public UserDto updateUser(long id, UserDto userDto) {
        User user = UserMapper.fromUserDto(userDto);
        return userRepository.updateUser(id, user);
    }

    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }
}
