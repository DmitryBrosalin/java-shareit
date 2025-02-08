package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items;

    public ItemDto addItem(Item item) {
        if (item.getName() == null || item.getName().isBlank() ||
                item.getDescription() == null || item.getDescription().isBlank() ||
                item.getAvailable() == null) {
            throw new BadRequestException("Поля вещи не могут быть пустыми.");
        }
        item.setId(items.size() + 1);
        items.put(item.getId(), item);
        return ItemMapper.toItemDto(item);
    }

    public ItemDto updateItem(long userId, long id, Item item) {
        Item oldItem = items.get(id);
        if (oldItem.getOwnerId() != userId) {
            throw new BadRequestException("Обновить данные вещи может только владелец.");
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.toItemDto(oldItem);
    }

    public ItemDto getItem(long id) {
        try {
            Optional<Item> itemOpt = Optional.of(items.get(id));
            return ItemMapper.toItemDto(itemOpt.get());
        } catch (NullPointerException e) {
            throw new NotFoundException("Вещь не найдена.");
        }
    }

    public List<ItemDto> getItemsForOwner(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return items.values().stream()
                    .filter(Item::getAvailable)
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                            item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .map(ItemMapper::toItemDto)
                    .toList();
        }
    }
}
