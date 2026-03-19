package com.books.bonnets.librarian.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerDto (@NotBlank(message = "first name is required")String firstName ,
                           @NotBlank(message = "last name is required")String lastName ,
                           @NotBlank(message = "email is required") String email ,
                           String phoneNumber) {
}
