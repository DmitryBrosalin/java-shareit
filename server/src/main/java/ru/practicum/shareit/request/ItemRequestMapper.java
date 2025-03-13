package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ItemRequestMapper {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemRequest fromItemRequestDto(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setItems(itemRepository.findByItemRequestId(itemRequest.getId())
                .stream().map(itemMapper::toItemForRequestListDto).collect(Collectors.toList()));
        return itemRequestDto;
    }
}
