package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    ItemDto addItem(Item item);

    ItemDto updateItem(long userId, long id, Item item);

    ItemDto getItem(long id);

    List<ItemDto> getItemsForOwner(long userId);

    List<ItemDto> searchItems(String text);
}
