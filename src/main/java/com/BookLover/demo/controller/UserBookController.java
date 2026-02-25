package com.BookLover.demo.controller;


import com.BookLover.demo.dto.mapper.UserBookMapper;
import com.BookLover.demo.dto.request.AddBookRequest;
import com.BookLover.demo.dto.request.ReviewRequest;
import com.BookLover.demo.dto.response.UserBookResponse;
import com.BookLover.demo.model.entity.UserBook;
import com.BookLover.demo.model.enums.ReadingStatus;
import com.BookLover.demo.service.UserBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/books")
@RequiredArgsConstructor
public class UserBookController {

    private final UserBookService userBookService;
    private final UserBookMapper userBookMapper;

    //POST /api/users/{userId}/books/{bookId}?status=WANT_TO_READ
    // Добавить книгу в свою библиотеку
    @PostMapping
    public ResponseEntity<UserBookResponse> addBookToUser(
            @PathVariable Long userId,
            @Valid @RequestBody AddBookRequest request) {  // используем DTO!

        UserBook userBook = userBookService.addBookToUser(
                userId,
                request.getBookId(),
                request.getStatus()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userBookMapper.toResponse(userBook));
    }

    // GET /api/users/{userId}/books - все книги пользователя
    // GET /api/users/{userId}/books?status=READING - только по статусу
    @GetMapping
    public ResponseEntity<List<UserBookResponse>> getUserBooks(
            @PathVariable Long userId,
            @RequestParam(required = false) ReadingStatus status) {

        List<UserBook> books = (status != null)
                ? userBookService.getUserBooksByStatus(userId, status)
                : userBookService.getUserBooks(userId);

        List<UserBookResponse> responses = books.stream()
                .map(userBookMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // GET /api/users/{userId}/books/{bookId} - получить конкретную книгу из библиотеки
    @GetMapping("/{bookId}")
    public ResponseEntity<UserBook> getUserBook(
            @PathVariable Long userId,
            @PathVariable Long bookId) {

        // У нас нет прямого метода в сервисе, но можем получить все и отфильтровать?
        // Лучше добавить метод в сервис, но пока так:
        List<UserBook> books = userBookService.getUserBooks(userId);
        return books.stream()
                .filter(ub -> ub.getBook().getId().equals(bookId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /api/users/{userId}/books/{bookId}/status?status=READING
    // Изменить статус книги
    @PatchMapping("/{bookId}/status")
    public ResponseEntity<UserBook> updateBookStatus(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestParam ReadingStatus status) {

        UserBook updated = userBookService.updateBookStatus(userId, bookId, status);
        return ResponseEntity.ok(updated);
    }

    // POST /api/users/{userId}/books/{bookId}/review
    // Поставить оценку и оставить отзыв
    @PostMapping("/{bookId}/review")
    public ResponseEntity<UserBookResponse> addReview(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @Valid @RequestBody ReviewRequest request) {

        UserBook updated = userBookService.rateAndReviewBook(
                userId,
                bookId,
                request.getRating(),
                request.getReview()
        );

        return ResponseEntity.ok(userBookMapper.toResponse(updated));
    }


    // DELETE /api/users/{userId}/books/{bookId}
    // Удалить книгу из библиотеки
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> removeBookFromUser(
            @PathVariable Long userId,
            @PathVariable Long bookId) {

        userBookService.removeBookFromUser(userId, bookId);
        return ResponseEntity.noContent().build();
    }

    // GET /api/users/{userId}/books/stats - статистика по чтению
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getReadingStats(@PathVariable Long userId) {
        Map<String, Long> stats = Map.of(
                "wantToRead", userBookService.countUserBooksByStatus(userId, ReadingStatus.WANT_TO_READ),
                "reading", userBookService.countUserBooksByStatus(userId, ReadingStatus.READING),
                "read", userBookService.countUserBooksByStatus(userId, ReadingStatus.READ),
                "abandoned", userBookService.countUserBooksByStatus(userId, ReadingStatus.ABANDONED)
        );
        return ResponseEntity.ok(stats);
    }
}


