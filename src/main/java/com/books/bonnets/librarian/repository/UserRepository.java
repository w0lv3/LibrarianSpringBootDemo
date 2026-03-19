package com.books.bonnets.librarian.repository;

import com.books.bonnets.librarian.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, UserCustomRepository {
}
