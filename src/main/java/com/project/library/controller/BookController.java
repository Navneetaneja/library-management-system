package com.project.library.controller;

import com.project.library.entity.Book;
import com.project.library.models.WrapperResponse;
import com.project.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("create")
    public ResponseEntity<WrapperResponse<String>> createBook(
            @Valid @RequestBody Book book) {
        String created = bookService.createBook(book);
        WrapperResponse<String> response = new WrapperResponse<>(true, created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
