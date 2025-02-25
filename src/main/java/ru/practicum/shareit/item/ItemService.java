package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto addItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwnerId(userRepository.getUser(userId).getId());
        return itemRepository.addItem(item);
    }

    public ItemDto updateItem(long userId, long id, ItemDto itemDto) {
        userRepository.getUser(userId);
        Item item = ItemMapper.fromItemDto(itemDto);
        return itemRepository.updateItem(userId, id, item);
    }

    public ItemDto getItem(long id) {
        return itemRepository.getItem(id);
    }

    public List<ItemDto> getItemsForOwner(long userId) {
        userRepository.getUser(userId);
        return itemRepository.getItemsForOwner(userId);
    }

    public List<ItemDto> searchItems(String text) {
        return itemRepository.searchItems(text);
    }
}
