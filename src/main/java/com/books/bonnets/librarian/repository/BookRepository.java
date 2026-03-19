package com.books.bonnets.librarian.repository;

import com.books.bonnets.librarian.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer>{
}
