package com.BookLover.demo.repository;

import com.BookLover.demo.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Ищем книги, где название содержит текст (без учета регистра)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Ищем по автору
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Точный поиск по ISBN
    Optional<Book> findByIsbn(String isbn);
}
