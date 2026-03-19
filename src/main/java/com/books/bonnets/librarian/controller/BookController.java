package com.books.bonnets.librarian.controller;

import com.books.bonnets.librarian.dto.BookDto;
import com.books.bonnets.librarian.entity.Book;
import com.books.bonnets.librarian.mapper.BookMapper;
import com.books.bonnets.librarian.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    //Displays all books
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> bookDtos = bookService.getBookCache().values().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookDtos);
    }

    //Create multiple books
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/books")
    public ResponseEntity<List<BookDto>> addMultipleBooks(@RequestBody @Valid List<BookDto> bookDtoList){
        List<BookDto> savedBooks = new ArrayList<>();
        for (BookDto bookDto : bookDtoList) {
            Book book = bookMapper.toEntity(bookDto);
            savedBooks.add(bookMapper.toDto(bookService.save(book)));
        }
        return ResponseEntity.ok(savedBooks);
    }

    //Create a book
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/book")
    public ResponseEntity<BookDto> addBook(@RequestBody @Valid BookDto bookDto){
        Book book = bookMapper.toEntity(bookDto);
        return ResponseEntity.ok(bookMapper.toDto(bookService.save(book)));
    }

    //Update a book
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/book/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Integer id, @RequestBody @Valid BookDto bookDto){
        Book book = bookMapper.toEntity(bookDto);
        book.setId(id);
        return ResponseEntity.ok(bookMapper.toDto(bookService.save(book)));
    }

    //Patch a book
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/book/{id}")
    public ResponseEntity<BookDto> patchBook(@PathVariable Integer id,
                                       @RequestBody Map<String, Object> patchPayload) {

        return ResponseEntity.ok(bookMapper.toDto(bookService.patch(id, patchPayload)));
    }

    //Delete a book
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
