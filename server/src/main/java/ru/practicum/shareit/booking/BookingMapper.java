package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

public class BookingMapper {
    public static BookingDtoResponse toBookingDto(Booking booking) {
        return new BookingDtoResponse(booking.getId(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus(),
                booking.getStart(),
                booking.getEnd());
    }

    public static Booking fromBookingDto(BookingDtoRequest bookingDto, Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }
}
