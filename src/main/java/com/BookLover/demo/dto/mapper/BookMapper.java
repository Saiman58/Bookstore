package com.BookLover.demo.dto.mapper;

import com.BookLover.demo.dto.response.BookResponse;
import com.BookLover.demo.model.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .isbn(book.getIsbn())
                .pageCount(book.getPageCount())
                .publishedDate(book.getPublishedDate())
                .publisher(book.getPublisher())
                .coverImageUrl(book.getCoverImageUrl())
                .build();
    }
}
