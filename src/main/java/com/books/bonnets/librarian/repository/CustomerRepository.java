package com.books.bonnets.librarian.repository;

import com.books.bonnets.librarian.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer>, CustomerCustomRepository {
}
