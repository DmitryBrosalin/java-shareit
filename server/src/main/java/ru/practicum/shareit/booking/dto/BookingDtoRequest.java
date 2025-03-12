package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingDtoRequest {
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
