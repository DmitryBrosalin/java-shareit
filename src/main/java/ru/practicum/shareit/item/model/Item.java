package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;

    public Item(String name, String description, Boolean available) {
        this.setName(name);
        this.setDescription(description);
        this.setAvailable(available);
    }
}
