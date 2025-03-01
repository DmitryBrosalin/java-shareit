package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking save(Booking booking);

    Booking findById(long bookingId);

    List<Booking> findByBookerIdOrderByEndDesc(long bookerId);

    List<Booking> findByBookerIdAndStatusAndStartBeforeAndEndAfterOrderByEndDesc(long bookerId, BookingState status, LocalDateTime now1, LocalDateTime now2);

    List<Booking> findByBookerIdAndStatusAndEndBeforeOrderByEndDesc(long bookerId, BookingState status, LocalDateTime now);

    List<Booking> findByBookerIdAndStatusAndStartAfterOrderByEndDesc(long bookerId, BookingState status, LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByEndDesc(long bookerId, BookingState status);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item WHERE item.owner.id = ?1 ORDER BY b.end DESC")
    List<Booking> findByOwnerId(long ownerId);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item WHERE item.owner.id = ?1 AND " +
            "b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP AND " +
            "b.status LIKE 'APPROVED' ORDER BY b.end DESC")
    List<Booking> findByOwnerIdCurrent(long ownerId);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item WHERE item.owner.id = ?1 AND " +
            "b.end < CURRENT_TIMESTAMP AND " +
            "b.status LIKE 'APPROVED' ORDER BY b.end DESC")
    List<Booking> findByOwnerIdPast(long ownerId);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item WHERE item.owner.id = ?1 AND " +
            "b.start > CURRENT_TIMESTAMP AND " +
            "b.status LIKE 'APPROVED' ORDER BY b.end DESC")
    List<Booking> findByOwnerIdFuture(long ownerId);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item WHERE item.owner.id = ?1 AND " +
            "b.status LIKE ?2 ORDER BY b.end DESC")
    List<Booking> findByOwnerIdAndStatus(long ownerId, BookingState status);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.end < CURRENT_TIMESTAMP ORDER BY b.end DESC LIMIT 1")
    Booking findLastBooking(long itemId);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.start > CURRENT_TIMESTAMP ORDER BY b.start ASC LIMIT 1")
    Booking findNextBooking(long itemId);
}
