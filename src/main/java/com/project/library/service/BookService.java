package com.project.library.service;

import com.project.library.entity.Book;
import com.project.library.enums.AvailabilityStatus;
import com.project.library.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.UUID;

@Slf4j
@Service
public class BookService {
    private static final String BOOK_CREATED = "Book Created Successfully";
    private static final int MIN_YEAR = 1900;

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public String createBook(Book book) {
        String isbn = book.getIsbn().trim().toLowerCase();
        String title = book.getTitle().trim();
        String author = book.getAuthor().trim();
        if (bookRepository.existsByIsbn(isbn)) {
            throw new RuntimeException("Book with this ISBN Already Exists");
        }
        int year = book.getPublishedYear();
        int currentYear = Year.now().getValue();
        if (year < MIN_YEAR || year > currentYear) {
            throw new RuntimeException("Invalid Published Year, It should be between " +
                                               MIN_YEAR + " and " + currentYear);
        }
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        UUID bookId = bookRepository.save(book).getId();
        log.info("Book is created with id : {}", bookId);
        return BOOK_CREATED;
    }
}
