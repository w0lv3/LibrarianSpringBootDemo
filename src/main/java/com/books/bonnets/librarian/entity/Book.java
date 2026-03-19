package com.books.bonnets.librarian.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Data // Lombok generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString //make use of exclude @ToString{exclude = "email"} to not show specific fields.
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private Integer id;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "available", nullable = false)
    private boolean available;

}
