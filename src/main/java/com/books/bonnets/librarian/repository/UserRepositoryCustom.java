package com.books.bonnets.librarian.repository;

import com.books.bonnets.librarian.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    void deleteByUsername(String username);
    Optional<User> findByUsername(String username);
}
