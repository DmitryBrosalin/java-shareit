package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto addItem(long userId, Item item) {
        item.setOwnerId(userRepository.getUser(userId).getId());
        return itemRepository.addItem(item);
    }

    public ItemDto updateItem(long userId, long id, Item item) {
        userRepository.getUser(userId);
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
