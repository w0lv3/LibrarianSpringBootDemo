package com.books.bonnets.librarian.utils.exceptions;

public class InvalidBookException extends RuntimeException{
    public InvalidBookException(String message) {
        super(message);
    }
}
