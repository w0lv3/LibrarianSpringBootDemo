package com.books.bonnets.librarian.utils.exceptions;

public class InvalidCustomerException extends RuntimeException{
    public InvalidCustomerException(String message) {
        super(message);
    }
}
