package com.books.bonnets.librarian.service;

import com.books.bonnets.librarian.entity.Book;
import com.books.bonnets.librarian.repository.BookRepository;
import com.books.bonnets.librarian.utils.exceptions.InvalidBookException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final HashMap<Integer, Book> bookCache = new HashMap<>();

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void prefillCache() {
        bookRepository.findAll().forEach(book -> bookCache.put(book.getId(), book));
    }

    @Override
    public HashMap<Integer, Book> getBookCache() {
        return bookCache;
    }

    @Transactional
    @Override
    public Book findById(Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new InvalidBookException("Book not found!"));
    }

    @Transactional
    @Override
    public Book patch(Integer id, Map<String, Object> bookPayload) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new InvalidBookException("Book not found!"));

        bookPayload.forEach((key, value) -> {
            switch (key) {
                case "isbn" -> book.setIsbn((String) value);
                case "title" -> book.setTitle((String) value);
                case "author" -> book.setAuthor((String) value);
                case "price" -> book.setPrice(((Number) value).doubleValue());
                case "quantity" -> book.setQuantity(((Number) value).intValue());
                case "available" -> book.setAvailable((Boolean) value);
            }
        });

        Book savedBook = bookRepository.save(book);
        bookCache.put(savedBook.getId(), savedBook);
        return savedBook;
    }

    @Transactional
    @Override
    public Book save(Book book) {
        Book savedBook = bookRepository.save(book);
        bookCache.put(savedBook.getId(), savedBook);
        return savedBook;
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        bookRepository.deleteById(id);
        bookCache.remove(id);
    }
}