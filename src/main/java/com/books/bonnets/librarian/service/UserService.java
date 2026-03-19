package com.books.bonnets.librarian.service;

import com.books.bonnets.librarian.entity.User;

import java.time.LocalDateTime;

public interface UserService {
    User save(User user);
    User findByUsername(String username);
    void deleteById(Integer user);
    void deleteByUsername(String username);
    void saveRefreshToken(String username, String refreshToken, LocalDateTime expiresAt);
    User findByRefreshToken(String refreshToken);
}
