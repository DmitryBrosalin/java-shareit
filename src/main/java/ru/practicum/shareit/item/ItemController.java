package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody Item item) {
        return itemService.addItem(userId, item);
    }

    @PatchMapping(value = "/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long id,
                              @RequestBody @Valid Item item) {
        return itemService.updateItem(userId, id, item);
    }

    @GetMapping(value = "/{id}")
    public ItemDto getItem(@PathVariable long id) {
        return itemService.getItem(id);
    }

    @GetMapping
    public List<ItemDto> getItemsForOwner(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsForOwner(userId);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }
}
