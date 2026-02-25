package com.BookLover.demo.exception;

public class BookAlreadyAddedException extends RuntimeException{
    public BookAlreadyAddedException(String message) {
        super(message);
    }
}
