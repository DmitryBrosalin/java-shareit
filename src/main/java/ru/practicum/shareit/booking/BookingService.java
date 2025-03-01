package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoFromController;
import ru.practicum.shareit.booking.dto.BookingDtoToController;
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

    public BookingDtoToController addBooking(BookingDtoFromController bookingDto, long userId) {
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

    public BookingDtoToController approveBooking(long bookingId, long ownerId, boolean isApproved) {
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

    public BookingDtoToController getBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new BadRequestException("Просматривать бронирование может только владелец вещи или автор бронирования.");
        }
        return BookingMapper.toBookingDto(booking);
    }

    public List<BookingDtoToController> getBookingsForUser(long userId, String state) {
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

    public List<BookingDtoToController> getBookingsForOwner(long ownerId, String state) {
        if (userRepository.findById(ownerId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        state = state.toUpperCase();
        return switch (state) {
            case ("ALL") -> bookingRepository.findByOwnerId(ownerId)
                    .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case ("CURRENT") ->
                    bookingRepository.findByOwnerIdCurrent(ownerId)
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case ("PAST") ->
                    bookingRepository.findByOwnerIdPast(ownerId)
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case ("FUTURE") ->
                    bookingRepository.findByOwnerIdFuture(ownerId)
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case ("WAITING") -> bookingRepository.findByOwnerIdAndStatus(ownerId, BookingState.WAITING)
                    .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case ("REJECTED") -> bookingRepository.findByOwnerIdAndStatus(ownerId, BookingState.REJECTED)
                    .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            default -> throw new BadRequestException("Неизвестное состояние бронирования.");
        };
    }

    private void validateBooking(BookingDtoFromController bookingDto, long userId) {
        if (userRepository.findById(userId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (itemRepository.findById(bookingDto.getItemId()) == null) {
            throw new NotFoundException("Вещь не найдена.");
        }
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new BadRequestException("Ошибка при вводе времени бронирования.");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now()) || bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Бронирование не может быть в прошлом");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new BadRequestException("Бронирование должно иметь длительность");
        }
    }
}
