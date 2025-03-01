package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoToController;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ItemMapper {
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemDto toItemDto(Item item) {
        Booking lastBooking = bookingRepository.findLastBooking(item.getId());
        Booking nextBooking = bookingRepository.findNextBooking(item.getId());
        List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(item.getId());
        BookingDtoToController lastBookingDto = null;
        BookingDtoToController nextBookingDto = null;
        List<CommentDto> commentsDto = null;
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
                commentsDto);
    }

    public static Item fromItemDto(ItemDto itemDto) {
        return new Item(itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable());
    }
}
