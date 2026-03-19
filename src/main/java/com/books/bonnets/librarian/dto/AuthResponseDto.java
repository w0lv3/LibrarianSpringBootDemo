package com.books.bonnets.librarian.dto;

public record AuthResponseDto(String token,
                              String refreshToken,
                              String username){
}
