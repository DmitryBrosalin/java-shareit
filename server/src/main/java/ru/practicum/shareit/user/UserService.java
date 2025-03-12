package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.fromUserDto(userDto);
        validateEmail(user.getEmail());
        return UserMapper.toUserDto(userRepository.save(user));
    }

    public UserDto getUser(long id) {
        if (userRepository.findById(id) != null) {
            return UserMapper.toUserDto(userRepository.findById(id));
        } else {
                throw new NotFoundException("Пользователь не найден.");
        }
    }

    public UserDto updateUser(long id, UserDto userDto) {
        User user = UserMapper.fromUserDto(userDto);
        User oldUser = userRepository.findById(id);
        validateEmail(user.getEmail());
        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            oldUser.setEmail(user.getEmail());
        }
        return UserMapper.toUserDto(userRepository.save(oldUser));
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    private void validateEmail(String email) {
        if (userRepository.findByEmail(email) != null) {
            throw new InternalServerException("Email уже используется.");
        }
    }
}
