package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, long userId) {
        validateUser(userId);
        ItemRequest itemRequest = itemRequestMapper.fromItemRequestDto(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setUser(userRepository.findById(userId));
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    public List<ItemRequestDto> getItemRequestsForUser(long userId) {
        validateUser(userId);
        return itemRequestRepository.findByUserId(userId).stream().map(itemRequestMapper::toItemRequestDto).toList();
    }

    public List<ItemRequestDto> getAllItemRequests(long userId) {
        validateUser(userId);
        return itemRequestRepository.findByUserIdNot(userId).stream().map(itemRequestMapper::toItemRequestDto).toList();
    }

    public ItemRequestDto getItemRequest(long id) {
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.findById(id));
    }

    private void validateUser(long userId) {
        if (userRepository.findById(userId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
    }
}
