package com.BookLover.demo.service;

import com.BookLover.demo.exception.BookNotFoundException;
import com.BookLover.demo.model.entity.Book;
import com.BookLover.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    // Сохранить книгу (если есть по ISBN - вернуть существующую)
    @Override
    public Book saveBook(Book book) {

        if (book.getIsbn() != null && book.getIsbn().isEmpty()) {
            return bookRepository.findByIsbn(book.getIsbn())
                    .orElseGet(() -> bookRepository.save(book));
        }
        return bookRepository.save(book);
    }

    // Найти книгу по ID
    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Книга с id " + id + " не найдена"));
    }

    // Поиск книг по названию
    @Override
    public List<Book> searchBooksByTitle(String title) {

        if (title == null || title.trim().isEmpty()) {
            return List.of();
        }
        return bookRepository.findByTitleContainingIgnoreCase(title.trim());
    }

    // Поиск книг по автору
    @Override
    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return List.of();
        }
        return bookRepository.findByAuthorContainingIgnoreCase(author.trim());
    }

    // Найти книгу по ISBN
    @Override
    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Книга с ISBN " + isbn + " не найдена"));
    }

    // Получить все книги
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Удалить книгу
    @Override
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}
