package com.books.bonnets.librarian.service;

import com.books.bonnets.librarian.entity.Book;

import java.util.HashMap;
import java.util.Map;

public interface BookService {

    HashMap<Integer, Book> getBookCache();

    Book findById(Integer id);

    Book patch(Integer id, Map<String, Object> bookPayload);

    Book save(Book book);

    void deleteById(Integer id);
}
