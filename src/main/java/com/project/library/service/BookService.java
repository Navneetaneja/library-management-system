package com.project.library.service;

import com.project.library.entity.Book;
import com.project.library.enums.AvailabilityStatus;
import com.project.library.models.BookDetails;
import com.project.library.repository.BookRepository;
import com.project.library.repository.WishlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class BookService {
    private static final String BOOK_CREATED = "Book Created Successfully";
    private static final String BOOK_MODIFIED = "Book Modified Successfully";
    private static final String BOOK_STATUS_UPDATED = "Book Status Updated Successfully";
    private static final String BOOK_DELETED = "Book Deleted Successfully";
    private static final int MIN_YEAR = 1900;
    private static final int WISHLIST_USERS_SIZE = 10;

    private final BookRepository bookRepository;
    private final WishlistRepository wishlistRepository;

    public BookService(BookRepository bookRepository,
                       WishlistRepository wishlistRepository) {
        this.bookRepository = bookRepository;
        this.wishlistRepository = wishlistRepository;
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
        if (null != existingBook &&
                AvailabilityStatus.AVAILABLE == bookDetails.getAvailabilityStatus()) {
            CompletableFuture.runAsync(() -> sendNotificationToWishlistedUsers(
                    existingBook));
        }
        return BOOK_CREATED;
    }

    protected UUID getBookIdByIsbn(String isbn) {
        String bookIsbn = isbn.trim();
        if (bookIsbn.isEmpty()) {
            throw new RuntimeException("Book ISBN is Empty");
        }
        Book book = bookRepository.findByIsbn(bookIsbn.toLowerCase());
        if (null == book || book.isDeleted()) {
            return null;
        }
        return book.getId();
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

    public Page<BookDetails> searchBooks(int page, int size, String searchKeyword) {
        Pageable pageable = PageRequest.of(page, size);
        String search = String.format("%%%s%%", searchKeyword.trim().toLowerCase());
        return bookRepository.searchBooks(search, pageable);
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
        boolean sendWishlistNotification = false;
        if (null != bookDetails.getAvailabilityStatus()) {
            if (AvailabilityStatus.BORROWED == existingBook.getAvailabilityStatus() &&
                    AvailabilityStatus.AVAILABLE == bookDetails.getAvailabilityStatus()) {
                sendWishlistNotification = true;
            }
            existingBook.setAvailabilityStatus(bookDetails.getAvailabilityStatus());
        }
        bookRepository.save(existingBook);
        log.info("Book with id : {} is modified", existingBook.getId());
        if (sendWishlistNotification) {
            CompletableFuture.runAsync(() -> sendNotificationToWishlistedUsers(
                    existingBook));
        }
        return BOOK_MODIFIED;
    }

    public String updateStatus(String isbn, AvailabilityStatus status) {
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
        if (AvailabilityStatus.AVAILABLE == status) {
            CompletableFuture.runAsync(() -> sendNotificationToWishlistedUsers(
                    existingBook));
        }
        return BOOK_STATUS_UPDATED;
    }

    private void sendNotificationToWishlistedUsers(Book book) {
        int page = 0;
        Page<UUID> users;
        do {
            Pageable pageable = PageRequest.of(page, WISHLIST_USERS_SIZE);
            users = wishlistRepository.getWishlistedUsersForBook(book.getId(), pageable);
            users.forEach(userId -> log.info(
                    "Notification prepared for user_id : {} Book [{}] is now available",
                    userId, book.getTitle()));
            page++;
        } while (users.hasNext());
    }

    public String deleteBook(String isbn) {
        String bookIsbn = isbn.trim().toLowerCase();
        Book existingBook = bookRepository.findByIsbn(bookIsbn);
        if (null == existingBook || existingBook.isDeleted()) {
            throw new RuntimeException("Book not found with this ISBN");
        }
        existingBook.setDeleted(true);
        bookRepository.save(existingBook);
        log.info("Book with id : {} is deleted", existingBook.getId());
        return BOOK_DELETED;
    }

}
