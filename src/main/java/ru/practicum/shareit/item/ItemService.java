package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public ItemDto addItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.fromItemDto(itemDto);
        if (userRepository.findById(userId) != null) {
            item.setOwner(userRepository.findById(userId));
            if (item.getName() == null || item.getName().isBlank() ||
                    item.getDescription() == null || item.getDescription().isBlank() ||
                    item.getAvailable() == null) {
                throw new BadRequestException("Поля вещи не могут быть пустыми.");
            }
            return itemMapper.toItemDto(itemRepository.save(item));
        } else {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    public ItemDto updateItem(long userId, long id, ItemDto itemDto) {
        if (userRepository.findById(userId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        Item item = ItemMapper.fromItemDto(itemDto);
        Item oldItem = itemRepository.findById(id);
        if (oldItem.getOwner().getId() != userId) {
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
        return itemMapper.toItemDto(itemRepository.save(oldItem));
    }

    public ItemDto getItem(long id, long userId) {
        Item item = itemRepository.findById(id);
        if (itemRepository.findById(id) == null) {
            throw new NotFoundException("Вещь не найдена.");
        }
        ItemDto itemDto = itemMapper.toItemDto(item);
        if (userId != item.getOwner().getId()) {
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }
        return itemDto;
    }

    public List<ItemDto> getItemsForOwner(long userId) {
        userRepository.findById(userId);
        return itemRepository.findByOwnerId(userId).stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String text) {
        if (!text.isBlank()) {
            return itemRepository.findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, text)
                    .stream().map(itemMapper::toItemDto).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public CommentDto addComment(long userId, long id, CommentDto commentDto) {
        List<Booking> b = bookingRepository.findByBookerIdAndStatusAndEndBeforeOrderByEndDesc(userId, BookingState.APPROVED,
                LocalDateTime.now());
        if (b == null || b.stream().noneMatch(booking -> booking.getItem().getId() == id)) {
            throw new BadRequestException("Отзыв может оставить только пользователь, который брал в аренду эту вещь.");
        }
        Comment comment = CommentMapper.fromCommentDto(commentDto);
        comment.setItem(itemRepository.findById(id));
        comment.setAuthorName(userRepository.findById(userId).getName());
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }
}
