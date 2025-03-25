package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()
                || userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new BadRequestException("Email и имя пользователя не могут быть пустыми.");
        }
        log.info("Creating user {}", userDto);
        return userClient.addUser(userDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getUser(@PathVariable long id) {
        log.info("Get user {}", id);
        return userClient.getUser(id);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id, @RequestBody @Valid UserDto userDto) {
        log.info("Update user {}, {}", id, userDto);
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        log.info("Delete user {}", id);
        return userClient.deleteUser(id);
    }
}
