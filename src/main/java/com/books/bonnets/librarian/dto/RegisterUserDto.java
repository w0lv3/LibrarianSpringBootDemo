package com.books.bonnets.librarian.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterUserDto (@NotBlank String username,
                              @NotBlank String password,
                              @NotBlank String email){
}
