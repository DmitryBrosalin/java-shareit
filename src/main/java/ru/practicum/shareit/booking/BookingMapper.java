package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoFromController;
import ru.practicum.shareit.booking.dto.BookingDtoToController;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

public class BookingMapper {
    public static BookingDtoToController toBookingDto(Booking booking) {
        return new BookingDtoToController(booking.getId(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus(),
                booking.getStart(),
                booking.getEnd());
    }

    public static Booking fromBookingDto(BookingDtoFromController bookingDto, Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }
}
