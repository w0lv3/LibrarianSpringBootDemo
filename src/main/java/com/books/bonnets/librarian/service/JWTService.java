package com.books.bonnets.librarian.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

    // Generates a signed JWT token for the authenticated user
    String generateToken(UserDetails userDetails);

    // Generates a longer-lived refresh token
    String generateRefreshToken(UserDetails userDetails);

    // Extracts the username (subject) stored inside the token
    String extractUsername(String token);

    // Validates that the token belongs to the given user and is not expired
    boolean isTokenValid(String token, UserDetails userDetails);

    // Validates refresh token the same way, but for refresh-token lifetime
    boolean isRefreshTokenValid(String token, UserDetails userDetails);
}
