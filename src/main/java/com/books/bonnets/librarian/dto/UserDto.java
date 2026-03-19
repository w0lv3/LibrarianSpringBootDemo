package com.books.bonnets.librarian.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UserDto(@NotBlank String username,
                      @NotBlank String password,
                      @NotBlank String email,
                      @NotEmpty Set<String> roles) {
}
