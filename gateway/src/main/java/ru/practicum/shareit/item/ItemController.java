package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating item {}, userId = {}", itemDto, userId);

        if (itemDto.getName() == null || itemDto.getName().isBlank()
                || itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new BadRequestException("Поля вещи не могут быть пустыми.");
        }
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long id,
                              @RequestBody @Valid ItemDto itemDto) {
        log.info("Updating item {}, itemId = {}, userId = {}", itemDto, id, userId);
        return itemClient.updateItem(userId, id, itemDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long id) {
        log.info("Get item {}, userId = {}", id, userId);
        return itemClient.getItem(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsForOwner(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get items for owner {}", userId);
        return itemClient.getItemsForOwner(userId);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        log.info("Search item by text {}", text);
        return itemClient.searchItems(text);
    }

    @PostMapping(value = "/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long id,
                                 @RequestBody @Valid CommentDto commentDto) {
        log.info("Creating comment {} for item {}, userId = {}", commentDto, id, userId);
        return itemClient.addComment(userId, id, commentDto);
    }
}
