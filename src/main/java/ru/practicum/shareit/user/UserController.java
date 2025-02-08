package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @PatchMapping(value = "/{id}")
    public User updateUser(@PathVariable long id, @Valid @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
