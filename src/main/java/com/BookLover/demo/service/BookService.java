package com.BookLover.demo.service;

import com.BookLover.demo.model.entity.Book;

import java.util.List;

public interface BookService {

    // Сохранить книгу (если есть по ISBN - вернуть существующую)
    Book saveBook(Book book);

    // Найти книгу по ID
    Book getBookById(Long id);

    // Поиск книг по названию
    List<Book> searchBooksByTitle(String title);

    // Поиск книг по автору
    List<Book> searchBooksByAuthor(String author);

    // Найти книгу по ISBN
    Book getBookByIsbn(String isbn);

    // Получить все книги
    List<Book> getAllBooks();

    // Удалить книгу
    void deleteBook(Long id);
}
