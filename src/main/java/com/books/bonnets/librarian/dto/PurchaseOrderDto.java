package com.books.bonnets.librarian.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PurchaseOrderDto(Integer id,
                               @NotNull(message = "Book ID is required") Integer bookId,
                               @NotNull(message = "Customer ID is required") Integer customerId,
                               LocalDate purchaseDate) {
}
