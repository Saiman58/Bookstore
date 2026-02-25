package com.BookLover.demo.service;

import com.BookLover.demo.exception.BookAlreadyAddedException;
import com.BookLover.demo.exception.BookNotFoundException;
import com.BookLover.demo.model.entity.Book;
import com.BookLover.demo.model.entity.User;
import com.BookLover.demo.model.entity.UserBook;
import com.BookLover.demo.model.enums.ReadingStatus;
import com.BookLover.demo.repository.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserBookServiceImpl implements UserBookService {

    private final UserBookRepository userBookRepository;
    private final UserService userService;
    private final BookService bookService;

    // Добавить книгу в библиотеку пользователя
    @Override
    public UserBook addBookToUser(Long userId, Long bookId, ReadingStatus status) {
        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId);

        var existing = userBookRepository.findByUserAndBookId(user, bookId);
        if (existing.isPresent()) {
            throw new BookAlreadyAddedException("Книга уже есть в библиотеке пользователя");
        }

        UserBook userBook = new UserBook();
        userBook.setUser(user);
        userBook.setBook(book);
        userBook.setStatus(status);

        if (status == ReadingStatus.READING) {
            userBook.setStartedAt(LocalDateTime.now());
        }
        return userBookRepository.save(userBook);
    }

    // Изменить статус книги у пользователя
    @Override
    public UserBook updateBookStatus(Long userId, Long bookId, ReadingStatus newStatus) {

        UserBook userBook = getUserBook(userId, bookId);

        if (newStatus == ReadingStatus.READING && userBook.getStartedAt() == null) {
            userBook.setStartedAt(LocalDateTime.now());
        } else if (newStatus == ReadingStatus.READ && userBook.getFinishedAt() == null) {
            userBook.setFinishedAt(LocalDateTime.now());
        } else if (newStatus == ReadingStatus.WANT_TO_READ) {
            // Если вернул в "хочу прочитать" - сбрасываем даты
            userBook.setStartedAt(null);
            userBook.setFinishedAt(null);
        }

        userBook.setStatus(newStatus);
        return userBookRepository.save(userBook);
    }


    // Поставить оценку и написать отзыв
    @Override
    public UserBook rateAndReviewBook(Long userId, Long bookId, Integer rating, String review) {

        UserBook userBook = getUserBook(userId, bookId);

        if (rating != null && (rating < 1 || rating > 5)) {
            throw new IllegalArgumentException("Оценка должна быть от 1 до 5");
        }

        userBook.setUserRating(rating);
        userBook.setUserReview(review);

        return userBookRepository.save(userBook);
    }

    // Получить все книги пользователя
    @Override
    public List<UserBook> getUserBooks(Long userId) {
        User user = userService.getUserById(userId);
        return userBookRepository.findByUser(user);
    }

    //Получить книги пользователя по статусу
    @Override
    public List<UserBook> getUserBooksByStatus(Long userId, ReadingStatus status) {
        User user = userService.getUserById(userId);
        return userBookRepository.findByUser(user);
    }

    // Удалить книгу из библиотеки пользователя
    @Override
    public void removeBookFromUser(Long userId, Long bookId) {
        UserBook userBook = getUserBook(userId, bookId);
        userBookRepository.delete(userBook);

    }

    //Посчитать количество книг пользователя по статусу
    @Override
    public long countUserBooksByStatus(Long userId, ReadingStatus status) {
        User user = userService.getUserById(userId);
        return userBookRepository.countByUserAndStatus(user, status);
    }

    // Приватный вспомогательный метод
    private UserBook getUserBook(Long userId, Long bookId) {
        User user = userService.getUserById(userId);
        return userBookRepository.findByUserAndBookId(user, bookId)
                .orElseThrow(() -> new BookNotFoundException("Книга не найдена в библиотеке пользователя"));
    }
}
