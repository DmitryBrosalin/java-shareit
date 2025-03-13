package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.BadRequestException;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestBody @Valid BookItemRequestDto requestDto) {
		if (requestDto.getStart().isEqual(requestDto.getEnd()) || requestDto.getStart().isAfter(requestDto.getEnd())) {
			throw new BadRequestException("Бронирование должно иметь длительность");
		}
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
			@PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping(value = "/{bookingId}")
	public ResponseEntity<Object> approveBooking(@PathVariable long bookingId,
											 @RequestHeader("X-Sharer-User-Id") long ownerId,
											 @RequestParam boolean approved) {
		log.info("Patch booking {}, ownerId {} approved {}", bookingId, ownerId, approved);
		return bookingClient.approveBooking(bookingId, ownerId, approved);
	}

	@GetMapping(value = "/owner")
	public ResponseEntity<Object> getBookingsForOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
													  @RequestParam(required = false, defaultValue = "ALL") String state) {
		BookingState bookingState = BookingState.from(state)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
		log.info("Get bookings for owner {} with state {}", ownerId, state);
		return bookingClient.getBookingsForOwner(ownerId, bookingState);
	}
}
