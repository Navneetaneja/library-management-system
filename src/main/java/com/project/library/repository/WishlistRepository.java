package com.project.library.repository;

import com.project.library.entity.Wishlist;
import com.project.library.entity.primaryKey.WishlistId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, WishlistId> {

    @Query("select w.userId from Wishlist w where w.bookId = :bookId order by w.userId ")
    Page<UUID> getWishlistedUsersForBook(UUID bookId, Pageable pageable);
}
