package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsForUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getItemRequestsForUser(userId);
    }

    @GetMapping(path = "/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getAllItemRequests(userId);
    }

    @GetMapping(value = "/{id}")
    public ItemRequestDto getItemRequest(@PathVariable long id) {
        return itemRequestService.getItemRequest(id);
    }
}
