package com.books.bonnets.librarian.repository;

import com.books.bonnets.librarian.entity.Customer;

import java.util.Optional;

public interface CustomerCustomRepository {
    Optional<Customer> findByEmail(String username);
}
