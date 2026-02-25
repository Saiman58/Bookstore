package com.BookLover.demo.controller;

import com.BookLover.demo.model.entity.Book;
import com.BookLover.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    //GET /api/books/search?title=война+и+мир - поиск по названию
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author) {
        // @RequestParam - параметры из query строки: /search?title=война&author=толстой

        if (title != null && !title.isEmpty()) {
            return ResponseEntity.ok(bookService.searchBooksByTitle(title));
        }

        if (author != null && !author.isEmpty()) {
            return ResponseEntity.ok(bookService.searchBooksByAuthor(author));
        }

        // Если ничего не передали - возвращаем все книги
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    //GET /api/books/{id} - получить книгу по ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    //GET /api/books/isbn/{isbn} - получить книгу по ISBN
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    //POST /api/books - добавить новую книгу в каталог
    //(может пригодиться, если книги нет во внешнем API)
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);
        return ResponseEntity.status(201).body(savedBook);
    }

      // DELETE /api/books/{id} - удалить книгу из каталога
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
