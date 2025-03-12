package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForRequestListDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ItemMapper {
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    public ItemDto toItemDto(Item item) {
        Booking lastBooking = bookingRepository.findLastBooking(item.getId());
        Booking nextBooking = bookingRepository.findNextBooking(item.getId());
        List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(item.getId());
        ItemRequest itemRequest = item.getItemRequest();
        BookingDtoResponse lastBookingDto = null;
        BookingDtoResponse nextBookingDto = null;
        List<CommentDto> commentsDto = null;
        long itemRequestId = 0;
        if (itemRequest != null) {
            itemRequestId = itemRequest.getId();
        }
        if (lastBooking != null) {
            lastBookingDto = BookingMapper.toBookingDto(lastBooking);
        }
        if (nextBooking != null) {
            nextBookingDto = BookingMapper.toBookingDto(nextBooking);
        }
        if (comments != null) {
            commentsDto = comments.stream().map(CommentMapper::toCommentDto).toList();
        }
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBookingDto,
                nextBookingDto,
                commentsDto,
                itemRequestId);
    }

    public Item fromItemDto(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId());
        item.setItemRequest(itemRequest);
        return item;
    }

    public static ItemForRequestListDto toItemForRequestListDto(Item item) {
        return new ItemForRequestListDto(item.getId(), item.getName(), item.getOwner().getId());
    }
}
