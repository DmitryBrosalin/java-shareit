package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    ItemRequest findById(long requestId);

    ItemRequest save(ItemRequest itemRequest);

    List<ItemRequest> findByUserId(long userId);

    List<ItemRequest> findByUserIdNot(long userId);
}
