package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse addBooking(@RequestBody BookingDtoRequest bookingDto,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.addBooking(bookingDto, userId);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDtoResponse approveBooking(@PathVariable long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") long ownerId,
                                             @RequestParam boolean approved) {
        return bookingService.approveBooking(bookingId, ownerId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDtoResponse getBooking(@PathVariable long bookingId,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoResponse> getBookingsForUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsForUser(userId, state);
    }

    @GetMapping(value = "/owner")
    public List<BookingDtoResponse> getBookingsForOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                        @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsForOwner(ownerId, state);
    }
}
