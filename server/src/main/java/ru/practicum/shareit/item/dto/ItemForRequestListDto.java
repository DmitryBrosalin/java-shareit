package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ItemForRequestListDto {
    private long id;
    private String name;
    private long ownerId;
}
