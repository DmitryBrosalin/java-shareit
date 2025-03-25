package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingDtoResponse addBooking(BookingDtoRequest bookingDto, long userId) {
        validateBooking(bookingDto, userId);
        Booking booking = BookingMapper.fromBookingDto(bookingDto, itemRepository.findById(bookingDto.getItemId()));
        if (booking.getItem().getAvailable()) {
            booking.setBooker(userRepository.findById(userId));
            booking.setStatus(BookingState.WAITING);
            booking.getItem().setAvailable(false);
            return BookingMapper.toBookingDto(bookingRepository.save(booking));
        } else {
            throw new BadRequestException("Вещь недоступна для бронирования.");
        }
    }

    public BookingDtoResponse approveBooking(long bookingId, long ownerId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new BadRequestException("Подтверждать или отклонять запрос на бронирование может только владелец вещи.");
        }
        if (isApproved) {
            booking.setStatus(BookingState.APPROVED);
        } else {
            booking.setStatus(BookingState.REJECTED);
            booking.getItem().setAvailable(true);
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    public BookingDtoResponse getBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new BadRequestException("Просматривать бронирование может только владелец вещи или автор бронирования.");
        }
        return BookingMapper.toBookingDto(booking);
    }

    public List<BookingDtoResponse> getBookingsForUser(long userId, String state) {
        if (userRepository.findById(userId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        state = state.toUpperCase();
        return switch (state) {
            case ("ALL") -> bookingRepository.findByBookerIdOrderByEndDesc(userId)
                    .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case ("CURRENT") ->
                    bookingRepository.findByBookerIdAndStatusAndStartBeforeAndEndAfterOrderByEndDesc(userId, BookingState.APPROVED, LocalDateTime.now(), LocalDateTime.now())
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case ("PAST") ->
                    bookingRepository.findByBookerIdAndStatusAndEndBeforeOrderByEndDesc(userId, BookingState.APPROVED, LocalDateTime.now())
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case ("FUTURE") ->
                    bookingRepository.findByBookerIdAndStatusAndStartAfterOrderByEndDesc(userId, BookingState.APPROVED, LocalDateTime.now())
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case ("WAITING") -> bookingRepository.findByBookerIdAndStatusOrderByEndDesc(userId, BookingState.WAITING)
                    .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case ("REJECTED") -> bookingRepository.findByBookerIdAndStatusOrderByEndDesc(userId, BookingState.REJECTED)
                    .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            default -> throw new BadRequestException("Неизвестное состояние бронирования.");
        };
    }

    public List<BookingDtoResponse> getBookingsForOwner(long ownerId, String state) {
        if (userRepository.findById(ownerId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        BookingState bookingState = BookingState.valueOf(state.toUpperCase());
        return switch (bookingState) {
            case BookingState.ALL -> bookingRepository.findByOwnerId(ownerId)
                    .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case BookingState.CURRENT ->
                    bookingRepository.findByOwnerIdCurrent(ownerId, BookingState.APPROVED)
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case BookingState.PAST ->
                    bookingRepository.findByOwnerIdPast(ownerId, BookingState.APPROVED)
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case BookingState.FUTURE ->
                    bookingRepository.findByOwnerIdFuture(ownerId, BookingState.APPROVED)
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case BookingState.WAITING, BookingState.REJECTED -> bookingRepository.findByOwnerIdAndStatus(ownerId, bookingState)
                    .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            default -> throw new BadRequestException("Неизвестное состояние бронирования.");
        };
    }

    private void validateBooking(BookingDtoRequest bookingDto, long userId) {
        if (userRepository.findById(userId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (itemRepository.findById(bookingDto.getItemId()) == null) {
            throw new NotFoundException("Вещь не найдена.");
        }
    }
}
