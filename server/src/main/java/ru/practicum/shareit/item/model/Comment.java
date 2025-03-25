package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @Column(name = "author_name")
    private String authorName;
    @Column
    private String text;
    @Column
    private LocalDateTime created;
}
