package com.project.library.controller;

import com.project.library.models.WrapperResponse;
import com.project.library.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("create")
    public ResponseEntity<WrapperResponse<String>> createWishlist(
            @RequestParam(name = "email") String userEmail,
            @RequestParam(name = "isbn") String bookIsbn) {
        String created = wishlistService.createWishlist(userEmail, bookIsbn);
        WrapperResponse<String> wrapperResponse = new WrapperResponse<>(
                true, created);
        return new ResponseEntity<>(wrapperResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("delete")
    public ResponseEntity<WrapperResponse<String>> deleteWishlist(
            @RequestParam(name = "email") String userEmail,
            @RequestParam(name = "isbn") String bookIsbn) {
        String deleted = wishlistService.deleteWishlist(userEmail, bookIsbn);
        WrapperResponse<String> wrapperResponse = new WrapperResponse<>(
                true, deleted);
        return new ResponseEntity<>(wrapperResponse, HttpStatus.OK);
    }
}
