package com.project.library.service;

import com.project.library.entity.Book;
import com.project.library.enums.AvailabilityStatus;
import com.project.library.models.BookDetails;
import com.project.library.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.UUID;

@Slf4j
@Service
public class BookService {
    private static final String BOOK_CREATED = "Book Created Successfully";
    private static final String BOOK_MODIFIED = "Book Modified Successfully";
    private static final int MIN_YEAR = 1900;

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public String createBook(BookDetails bookDetails) {
        int year = bookDetails.getPublishedYear();
        validatePublishedYear(year);
        String isbn = bookDetails.getIsbn().trim().toLowerCase();
        Book existingBook = bookRepository.findByIsbn(isbn);
        if (null != existingBook && !existingBook.isDeleted()) {
            throw new RuntimeException("Book with this ISBN Already Exists");
        }
        String title = bookDetails.getTitle().trim();
        String author = bookDetails.getAuthor().trim();
        UUID bookId = null == existingBook ? UUID.randomUUID() : existingBook.getId();
        Book book = Book.builder()
                .id(bookId)
                .isbn(isbn)
                .title(title)
                .author(author)
                .publishedYear(year)
                .availabilityStatus(bookDetails.getAvailabilityStatus())
                .build();
        bookRepository.save(book);
        log.info("Book is created with id : {}", bookId);
        return BOOK_CREATED;
    }

    private void validatePublishedYear(int year) {
        int currentYear = Year.now().getValue();
        if (year < MIN_YEAR || year > currentYear) {
            throw new RuntimeException("Invalid Published Year, It should be between " +
                                               MIN_YEAR + " and " + currentYear);
        }
    }

    public Page<BookDetails> getAllBooks(int page, int size, String author,
                                         Integer publishedYear) {
        Pageable pageable = PageRequest.of(page, size);
        String authorFilter = null == author ? null : author.trim().toLowerCase();
        return bookRepository.getAllBooks(authorFilter, publishedYear, pageable);
    }

    public String updateBook(BookDetails bookDetails) {
        String isbn = null == bookDetails.getIsbn() ? null : bookDetails.getIsbn()
                .trim().toLowerCase();
        if (null == isbn || isbn.isEmpty()) {
            throw new RuntimeException("Invalid Book ISBN");
        }
        Book existingBook = bookRepository.findByIsbn(isbn);
        if (null == existingBook || existingBook.isDeleted()) {
            throw new RuntimeException("Book not found with this ISBN");
        }
        Integer year = bookDetails.getPublishedYear();
        if (null != year) {
            validatePublishedYear(year);
            existingBook.setPublishedYear(year);
        }
        String title = null == bookDetails.getTitle() ? null :
                bookDetails.getTitle().trim();
        if (null != title && !title.isEmpty()) {
            existingBook.setTitle(title);
        }
        String author = null == bookDetails.getAuthor() ? null :
                bookDetails.getAuthor().trim();
        if (null != author && !author.isEmpty()) {
            existingBook.setAuthor(author);
        }
        if (null != bookDetails.getAvailabilityStatus()) {
            existingBook.setAvailabilityStatus(bookDetails.getAvailabilityStatus());
            if (AvailabilityStatus.BORROWED == existingBook.getAvailabilityStatus() &&
                    AvailabilityStatus.AVAILABLE == bookDetails.getAvailabilityStatus()) {
//                CompletableFuture.runAsync(() -> )
            }
        }
        bookRepository.save(existingBook);
        log.info("Book with id : {} is modified", existingBook.getId());
        return BOOK_MODIFIED;
    }

    public void updateStatus(String isbn, AvailabilityStatus status) {
        String isbnValue = isbn.trim().toLowerCase();
        Book existingBook = bookRepository.findByIsbn(isbnValue);
        if (null == existingBook || existingBook.isDeleted()) {
            throw new RuntimeException("Book not found with this ISBN");
        }
        if (status == existingBook.getAvailabilityStatus()) {
            throw new RuntimeException("Book already have same availability status");
        }
        existingBook.setAvailabilityStatus(status);
        bookRepository.save(existingBook);
        log.info("Book with id : {} is updated with status : {}",
                 existingBook.getId(), status);
//        if(AvailabilityStatus.AVAILABLE == status) {
//            CompletableFuture.runAsync(() -> )
//        }
    }

}
