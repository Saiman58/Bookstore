package com.BookLover.demo.dto.request;

import com.BookLover.demo.model.enums.ReadingStatus;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class AddBookRequest {

    @NotNull(message = "ID книги обязателен")
    private Long bookId;

    private ReadingStatus status = ReadingStatus.WANT_TO_READ;
}
