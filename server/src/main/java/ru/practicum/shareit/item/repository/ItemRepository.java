package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item save(Item item);

    Item findById(long id);

    List<Item> findByOwnerId(long ownerId);

    List<Item> findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String text1, String text2);

    List<Item> findByItemRequestId(long id);
}
