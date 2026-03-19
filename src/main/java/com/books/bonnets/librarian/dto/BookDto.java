package com.books.bonnets.librarian.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record BookDto (@NotBlank(message = "ISBN is required")String isbn,
                       @NotBlank(message = "Title is required")String title,
                       @NotBlank(message = "Author is required")String author,
                       @Positive(message = "Price must be greater than zero")double price,
                       @Min(value = 0, message = "Quantity cannot be negative")int quantity,
                       boolean available){
}
