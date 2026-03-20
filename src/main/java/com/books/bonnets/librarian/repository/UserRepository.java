package com.books.bonnets.librarian.repository;

import com.books.bonnets.librarian.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
}
