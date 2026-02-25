package com.BookLover.demo.service;

import com.BookLover.demo.model.entity.UserBook;
import com.BookLover.demo.model.enums.ReadingStatus;

import java.util.List;

public interface UserBookService {

    // Добавить книгу в библиотеку пользователя
    UserBook addBookToUser(Long userId, Long bookId, ReadingStatus status);

    // Изменить статус книги у пользователя
    UserBook updateBookStatus(Long userId, Long bookId, ReadingStatus newStatus);

    // Поставить оценку и написать отзыв
    UserBook rateAndReviewBook(Long userId, Long bookId, Integer rating, String review);

    // Получить все книги пользователя
    List<UserBook> getUserBooks(Long userId);

    //Получить книги пользователя по статусу
    List<UserBook> getUserBooksByStatus(Long userId, ReadingStatus status);

    // Удалить книгу из библиотеки пользователя
    void removeBookFromUser(Long userId, Long bookId);

    //Посчитать количество книг пользователя по статусу
    long countUserBooksByStatus(Long userId, ReadingStatus status);
}
