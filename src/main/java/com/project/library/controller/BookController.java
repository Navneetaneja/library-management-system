package com.project.library.controller;

import com.project.library.enums.AvailabilityStatus;
import com.project.library.models.BookDetails;
import com.project.library.models.WrapperResponse;
import com.project.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("create")
    public ResponseEntity<WrapperResponse<String>> createBook(
            @Valid @RequestBody BookDetails bookDetails) {
        String created = bookService.createBook(bookDetails);
        WrapperResponse<String> response = new WrapperResponse<>(true, created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("list")
    public ResponseEntity<WrapperResponse<Page<BookDetails>>> getAllBooks(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "publishedYear", required = false) Integer publishedYear) {
        Page<BookDetails> allBooks = bookService.getAllBooks(page,
                                                             size,
                                                             author,
                                                             publishedYear);
        WrapperResponse<Page<BookDetails>> response = new WrapperResponse<>(
                true, allBooks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<WrapperResponse<Page<BookDetails>>> searchBooks(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "search") String searchKeyword) {
        Page<BookDetails> books = bookService.searchBooks(page, size, searchKeyword);
        WrapperResponse<Page<BookDetails>> response = new WrapperResponse<>(
                true, books);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<WrapperResponse<String>> updateBook(
            @RequestBody BookDetails bookDetails) {
        String modified = bookService.updateBook(bookDetails);
        WrapperResponse<String> response = new WrapperResponse<>(true, modified);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("update-status/{isbn}")
    public ResponseEntity<WrapperResponse<String>> updateBookStatus(
            @PathVariable("isbn") String isbn,
            @RequestParam(name = "status") AvailabilityStatus status,
            @RequestParam(name = "user-email", required = false) String userEmail) {
        String statusUpdated = bookService.updateStatus(isbn, status, userEmail);
        WrapperResponse<String> response = new WrapperResponse<>(
                true, statusUpdated);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("delete/{isbn}")
    public ResponseEntity<WrapperResponse<String>> deleteBook(
            @PathVariable("isbn") String isbn) {
        String deleted = bookService.deleteBook(isbn);
        WrapperResponse<String> response = new WrapperResponse<>(true, deleted);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
