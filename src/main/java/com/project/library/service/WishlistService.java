package com.project.library.service;

import com.project.library.entity.Wishlist;
import com.project.library.entity.primaryKey.WishlistId;
import com.project.library.repository.WishlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class WishlistService {
    private static final String CREATED_WISHLIST = "Wishlist Created Successfully";
    private static final String DELETED_WISHLIST = "Wishlist Deleted Successfully";

    private final WishlistRepository wishlistRepository;
    private final UserService userService;
    private final BookService bookService;

    public WishlistService(WishlistRepository wishlistRepository,
                           UserService userService,
                           BookService bookService) {
        this.wishlistRepository = wishlistRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    public String createWishlist(String userEmail,
                                 String bookIsbn) {
        WishlistId wishlistId = getWishlistId(userEmail, bookIsbn);
        Wishlist wishlist = new Wishlist(wishlistId.getUserId(), wishlistId.getBookId());
        wishlistRepository.save(wishlist);
        log.info("wishlist created successfully for user id : {} and book id : {}",
                 wishlistId.getUserId(), wishlistId.getBookId());
        return CREATED_WISHLIST;
    }

    private WishlistId getWishlistId(String userEmail,
                                     String bookIsbn) {
        UUID userId = userService.getUserIdByEmail(userEmail);
        if (null == userId) {
            throw new RuntimeException("User doesn't exist with this email");
        }
        UUID bookId = bookService.getBookIdByIsbn(bookIsbn);
        if (null == bookId) {
            throw new RuntimeException("Book not found with isbn");
        }
        return new WishlistId(userId, bookId);
    }

    public String deleteWishlist(String userEmail,
                                 String bookIsbn) {
        WishlistId wishlistId = getWishlistId(userEmail, bookIsbn);
        wishlistRepository.deleteById(wishlistId);
        log.info("wishlist deleted successfully for user id : {} and book id : {}",
                 wishlistId.getUserId(), wishlistId.getBookId());
        return DELETED_WISHLIST;
    }
}
