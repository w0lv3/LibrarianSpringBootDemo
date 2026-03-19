package com.books.bonnets.librarian.mapper;
import com.books.bonnets.librarian.dto.BookDto;
import com.books.bonnets.librarian.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);
    Book toEntity(BookDto bookDto);
}
