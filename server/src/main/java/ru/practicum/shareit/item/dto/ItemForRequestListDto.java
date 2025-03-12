package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ItemForRequestListDto {
    private long id;
    private String name;
    private long ownerId;
}
